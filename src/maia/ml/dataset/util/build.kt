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

/**
 * TODO: Comment
 */
class HeadersBuilder internal constructor(initialCapacity: Int? = null) {
    val headers = MutableDataColumnHeaders(initialCapacity)

    infix fun String.feature(type: DataType<*, *>) = headers.append(this, type, false)
    infix fun String.target(type: DataType<*, *>) = headers.append(this, type, true)
}

/**
 * TODO: Comment
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
 * TODO: Comment
 */
class HeaderAndDataRowBuilder(
) {
    private val headers = MutableDataColumnHeaders()
    private val values = ArrayList<Pair<DataRepresentation<*, *, in Any?>, Any?>?>()

    inner class HeaderWithoutValue<T> private constructor(
        name: String,
        type: DataType<*, *>,
        representation: DataRepresentation<*, *, T>?,
        isTarget: Boolean
    ) {
        internal constructor(
            name: String,
            representation: DataRepresentation<*, *, T>,
            isTarget: Boolean
        ): this(name, representation.dataType, representation, isTarget)

        internal constructor(
            name: String,
            type: DataType<*, *>,
            isTarget: Boolean
        ): this(name, type, null, isTarget)

        private val representation: DataRepresentation<*, *, T>?

        init {
            this@HeaderAndDataRowBuilder.headers.append(name, type, isTarget)
            this@HeaderAndDataRowBuilder.values.add(null)
            this.representation = if (representation != null)
                // Not-null safety: Just added the equivalent representation
                this@HeaderAndDataRowBuilder.headers.ownedEquivalent(representation)!!
            else
                null
        }

        operator fun timesAssign(value: T) {
            // Treating representation as Any? because we know the value's type matches
            this@HeaderAndDataRowBuilder.values.add(
                // Not-null safety: headerWithMissingValue ensures T == Nothing when representation == null
                representation!!.columnIndex,
                Pair(representation as DataRepresentation<*, *, Any?>, value)
            )
        }
    }

    infix fun <T> String.feature(representation: DataRepresentation<*, *, T>) =
        HeaderWithoutValue(this, representation, false)
    infix fun <T> String.target(representation: DataRepresentation<*, *, T>) =
        HeaderWithoutValue(this, representation, true)

    infix fun String.feature(type: DataType<*, *>) =
        headerWithMissingValue(this, type, false)
    infix fun String.target(type: DataType<*, *>) =
        headerWithMissingValue(this, type, true)

    internal fun toRow(): DataRow = object: DataRow {
        override val headers = this@HeaderAndDataRowBuilder.headers.readOnlyView
        private val values = this@HeaderAndDataRowBuilder.values.toTypedArray()

        override fun <T> getValue(
            representation : DataRepresentation<*, *, out T>
        ) : T = headers.ensureOwnership(representation) {
            val (inputRepr, value) = values[this.columnIndex] ?: throw MissingValue(this)
            convert(value, inputRepr)
        }
    }

    private fun headerWithMissingValue(
        name: String,
        type: DataType<*, *>,
        isTarget: Boolean
    ): HeaderWithoutValue<Nothing> {
        type.checkMissingValueSupport()
        return HeaderWithoutValue(name, type, isTarget)
    }

}

/**
 * TODO: Comment
 */
fun buildRow(
    block: HeaderAndDataRowBuilder.() -> Unit
): DataRow {
    val builder = HeaderAndDataRowBuilder()
    builder.block()
    return builder.toRow()
}
