package maia.ml.dataset.util

/*
 * Module for building data-rows programmatically.
 */

import maia.ml.dataset.DataRow
import maia.ml.dataset.error.MissingValue
import maia.ml.dataset.error.checkMissingValueSupport
import maia.ml.dataset.headers.MutableDataColumnHeaders
import maia.ml.dataset.headers.ensureOwnership
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.type.DataType
import maia.util.zip

/**
 * Context-class for the [buildHeaders] DSL.
 */
class HeadersBuilder internal constructor(initialCapacity: Int? = null) {
    /** The headers being built. */
    val headers = MutableDataColumnHeaders(initialCapacity)

    /**
     * Adds a new feature (non-target) header.
     *
     * @receiver
     *          The name of the header.
     * @param type
     *          The type of the header.
     */
    infix fun String.feature(type: DataType<*, *>) = headers.append(this, type, false)

    /**
     * Adds a new target header.
     *
     * @receiver
     *          The name of the header.
     * @param type
     *          The type of the header.
     */
    infix fun String.target(type: DataType<*, *>) = headers.append(this, type, true)
}

/**
 * Allows programmatic construction of headers using a Domain-Specific Language.
 *
 * E.g.
 * ```
 * val headers: MutableDataColumnHeaders = buildHeaders {
 *     // Name     Target/Feature      Type
 *     "columnA"   feature             MyDataType1()    // A feature column
 *     "columnB"   target              MyDataType2()    // A target column
 * }
 * ```
 *
 * @param initialCapacity
 *          The amount of initial storage to set aside for the headers. If the
 *          number of headers is known, this can be set to avoid reallocation.
 * @param block
 *          The block of code to execute to build the headers.
 * @return
 *          The headers.
 */
fun buildHeaders(
    initialCapacity: Int? = null,
    block: HeadersBuilder.() -> Unit
): MutableDataColumnHeaders {
    val builder = HeadersBuilder(initialCapacity)
    builder.block()
    return builder.headers
}

/**
 * Context-class for the [buildRow] DSL.
 */
class DataRowBuilder(
    initialCapacity: Int? = null
) {
    /** The headers being built. */
    private val headers = MutableDataColumnHeaders(initialCapacity)

    /** The (representation, value) pair assigned to each column, or null for columns with missing values. */
    private val values: ArrayList<Pair<DataRepresentation<*, *, in Any?>, Any?>?> = initialCapacity?.let {
        ArrayList(it)
    } ?: ArrayList()

    /**
     * Temporary class which caches the intermediate result of adding a header
     * before adding a value.
     */
    inner class HeaderWithoutValue<T> private constructor(
        name: String,
        type: DataType<*, *>,
        representation: DataRepresentation<*, *, T>?,
        isTarget: Boolean
    ) {
        /**
         * Constructor for when given a [representation][DataRepresentation].
         */
        internal constructor(
            name: String,
            representation: DataRepresentation<*, *, T>,
            isTarget: Boolean
        ): this(name, representation.dataType, representation, isTarget)

        /**
         * Constructor for when given a [data-type][DataType].
         */
        internal constructor(
            name: String,
            type: DataType<*, *>,
            isTarget: Boolean
        ): this(name, type, null, isTarget)

        private val representation: DataRepresentation<*, *, T>?

        init {
            // Add the header to the builder
            this@DataRowBuilder.headers.append(name, type, isTarget)

            // Add a placeholder empty value to the builder, will be overwritten
            // if the user assigns a value
            this@DataRowBuilder.values.add(null)

            // Cache the representation if we were constructed with one
            this.representation = if (representation != null)
                // Not-null safety: Just added the equivalent representation
                this@DataRowBuilder.headers.ownedEquivalent(representation)!!
            else
                null
        }

        /**
         * Assigns a value to the column.
         */
        operator fun timesAssign(value: T) {
            // Treating representation as Any? because we know the value's type matches the representation
            this@DataRowBuilder.values.add(
                // Not-null safety: headerWithMissingValue ensures T == Nothing when representation == null,
                // meaning this function cannot be called with a value
                representation!!.columnIndex,
                // Unchecked cast safety: Generic type T ensures representation and value match,
                //                        so pretend they are both Any?
                Pair(representation as DataRepresentation<*, *, in Any?>, value)
            )
        }
    }

    /**
     * Adds a new feature (non-target) header, with the option of later setting
     * a value.
     *
     * @receiver
     *          The name of the header.
     * @param representation
     *          The representation of the value that we might choose to supply.
     * @return [HeaderWithoutValue].
     */
    infix fun <T> String.feature(representation: DataRepresentation<*, *, T>) =
        HeaderWithoutValue(this, representation, false)

    /**
     * Adds a new target header, with the option of later setting
     * a value.
     *
     * @receiver
     *          The name of the header.
     * @param representation
     *          The representation of the value that we might choose to supply.
     * @return [HeaderWithoutValue].
     */
    infix fun <T> String.target(representation: DataRepresentation<*, *, T>) =
        HeaderWithoutValue(this, representation, true)

    /**
     * Adds a new feature (non-target) header without a value.
     *
     * @receiver
     *          The name of the header.
     * @param type
     *          The type of the header.
     */
    infix fun String.feature(type: DataType<*, *>) {
        headerWithMissingValue(this, type, false)
    }

    /**
     * Adds a new target header without a value.
     *
     * @receiver
     *          The name of the header.
     * @param type
     *          The type of the header.
     */
    infix fun String.target(type: DataType<*, *>) {
        headerWithMissingValue(this, type, true)
    }

    /**
     * Finalises the built row.
     */
    internal fun toRow(): DataRow {
        // Check all uninitialised columns support missing values
        for ((header, value) in zip(headers, values)) {
            if (value == null) header.checkMissingValueSupport()
        }

        return object : DataRow {
            override val headers = this@DataRowBuilder.headers.readOnlyView
            private val values = this@DataRowBuilder.values.toTypedArray()

            override fun <T> getValue(
                representation : DataRepresentation<*, *, out T>
            ) : T = headers.ensureOwnership(representation) {
                val (inputRepr, value) = values[this.columnIndex]
                    ?: throw MissingValue(this)
                convert(value, inputRepr)
            }
        }
    }

    /**
     * Adds a header without adding a value.
     */
    private fun headerWithMissingValue(
        name: String,
        type: DataType<*, *>,
        isTarget: Boolean
    ): HeaderWithoutValue<Nothing> {
        // Make sure the type supports missing values
        type.checkMissingValueSupport()

        return HeaderWithoutValue(name, type, isTarget)
    }

}

/**
 * Allows programmatic construction of data-rows using a Domain-Specific Language.
 *
 * E.g.
 * ```
 * val row: DataRow = buildRow {
 *     // Name     Target/Feature      Type                                 Value
 *
 *     // A feature column with a value (typed by representation)
 *     // i.e. myRepresentation: DataRepresentation<*, *, in T> && generateValue(): T
 *     "columnA"   feature             MyDataType1().myRepresentation   *=  generateValue()
 *
 *     // A target column without a value
 *     "columnB"   target              MyDataType2()
 * }
 * ```
 *
 * @param initialCapacity
 *          The amount of initial storage to set aside for the headers and
 *          values. If the number of columns is known, this can be set to avoid
 *          reallocation.
 * @param block
 *          The block of code to execute to build the row.
 * @return
 *          The row.
 */
fun buildRow(
    initialCapacity : Int? = null,
    block: DataRowBuilder.() -> Unit
): DataRow {
    val builder = DataRowBuilder(initialCapacity)
    builder.block()
    return builder.toRow()
}
