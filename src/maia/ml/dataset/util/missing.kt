package maia.ml.dataset.util

import maia.ml.dataset.DataRow
import maia.ml.dataset.error.MissingValue
import maia.ml.dataset.type.*

/**
 * Whether the value for a data-row at the given index is missing or not.
 *
 * @param index
 *          The index of the row to check for a missing value.
 * @return
 *          Whether the value is missing or not.
 */
fun DataRow.isMissing(representation : DataRepresentation<*, *, *>): Boolean {
    ifNotMissing(representation) {
        return false
    }
    return true
}

/**
 * TODO
 */
fun DataRow.isMissing(type : DataType<*, *>): Boolean {
    return isMissing(type.canonicalRepresentation)
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
inline fun <T> DataRow.ifNotMissing(
    representation : DataRepresentation<*, *, T>,
    block: (T) -> Unit
) {
    val value = try {
        getValue(representation)
    } catch (e: MissingValue) {
        return
    }
    block(value)
}
