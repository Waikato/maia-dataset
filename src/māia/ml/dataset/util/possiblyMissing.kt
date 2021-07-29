package māia.ml.dataset.util

import māia.util.ThenContinuationWithSuccessValue
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataRow
import māia.ml.dataset.type.*

/**
 * Gets the base-type of this data-type if it includes missing values,
 * or just the input [type] if not.
 *
 * @param type  The type to extract a base-type from.
 * @return      The base-type.
 */
fun getBaseIfMissing(type : DataType<*, *>) : DataType<*, *> {
    return if (type is DataTypeWithMissingValues<*, *, *, *, *>)
        type.base
    else
        type
}

/**
 * Casts a data-type to the given type, automatically traversing
 * the case where the data-type is wrapped by [DataTypeWithMissingValues].
 *
 * @param type      The data-type to check.
 * @return          The cast data-type, or null if casting wasn't possible.
 * @param T         The type to cast the data-type to.
 */
inline fun <reified T : DataType<*, *>> getPossiblyMissingOrNull(type : DataType<*, *>) : T? {
    // Get the base-type
    val baseType = getBaseIfMissing(type)

    return if (baseType is T)
        baseType
    else
        null
}

/**
 * Casts a data-type to the given type, automatically traversing
 * the case where the data-type is wrapped by [DataTypeWithMissingValues].
 *
 * @param type          The data-type to check.
 * @return              The cast data-type.
 * @param T             The type to cast the data-type to.
 * @throws Exception    If casting wasn't possible.
 */
inline fun <reified T : DataType<*, *>> getPossiblyMissing(type : DataType<*, *>) : T {
    return getPossiblyMissingOrNull(type) ?: throw Exception("Type is not of type '${T::class.simpleName}'")
}

/**
 * Checks if a data-type is of a particular type, or of that type but
 * with missing values.
 *
 * @param type      The data-type to check.
 * @return          If the data-type is of the given type.
 * @param T         The type to check the data-type for.
 */
inline fun <reified T : DataType<*, *>> isPossiblyMissing(type : DataType<*, *>) : Boolean {
    return getPossiblyMissingOrNull<T>(type) != null
}

/**
 * Creates an if-like context which operates on the condition that
 * the given data-type is of a particular type, or of that type but
 * with missing values.
 *
 * @param type      The data-type to check.
 * @return          A continuation for the given type.
 * @param T         The type to check the data-type for.
 */
inline fun <reified T : DataType<*, *>> ifIsPossiblyMissing(type : DataType<*, *>) : ThenContinuationWithSuccessValue<T> {
    val castType = getPossiblyMissingOrNull<T>(type)
    return if (castType != null)
        ThenContinuationWithSuccessValue(castType)
    else
        ThenContinuationWithSuccessValue()
}

/**
 * Creates a data-type which exposes this data-type as having missing values,
 * but with the set of missing values being empty.
 */
fun <I, X, D: DataType<I, X>> D.asNeverMissingValues(): WithMissingValues<I, X, D, I, X> {
    return WithMissingValues(
        this,
        NoMissingValuesConverter(),
        NoMissingValuesConverter()
    )
}

/**
 * Creates a data-type which exposes this data-type as having missing values,
 * but with the set of missing values being empty.
 */
fun <I, X, D: FiniteDataType<I, X>> D.asNeverMissingValues(): WithFiniteMissingValues<I, X, D, I, X> {
    return WithFiniteMissingValues(
        this,
        NoMissingValuesConverter(),
        NoMissingValuesConverter()
    )
}

/**
 * Whether the value for a data-row at the given index is missing or not.
 *
 * @param index
 *          The index of the row to check for a missing value.
 * @return
 *          Whether the value is missing or not.
 */
fun DataRow.isMissing(index: Int): Boolean {
    ifNotMissing(index) { _, _ ->
        return true
    }
    return false
}

/**
 * Performs the given action if the value at a certain index
 * is not missing.
 *
 * @receiver
 *          The data-row to check for a missing value.
 * @param index
 *          The column index to check for a missing value.
 * @param block
 *          The action to take if the value is present.
 */
inline fun DataRow.ifNotMissing(
    index: Int,
    block: (DataColumnHeader, Any?) -> Unit
) {
    val header = getColumnHeader(index)
    val type = header.type
    if (type is DataTypeWithMissingValues<*, *, *, *, *>) {
        val value = getColumn(index)
        if (type.isMissingInternalUnchecked(value)) {
            return
        } else {
            block(header, value)
        }
    } else {
        block(header, getColumn(index))
    }
}

/**
 * Converts a data-type to its missing-values equivalent if it is not already
 * capable of missing values, using the [NoMissingValuesConverter].
 *
 * @receiver
 *          The data-type to check/convert.
 * @return
 *          The receiver if it already can handle missing values, or an
 *          equivalent data-type which uses the [NoMissingValuesConverter] if
 *          not.
 */
fun DataType<*, *>.ensureMissingValues(): DataTypeWithMissingValues<*, *, *, *, *> {
    return if (this is DataTypeWithMissingValues<*, *, *, *, *>)
        this
    else if (this is FiniteDataType<*, *>)
        (this as FiniteDataType<Any?, Any?>).asNeverMissingValues()
    else
        (this as DataType<Any?, Any?>).asNeverMissingValues()
}
