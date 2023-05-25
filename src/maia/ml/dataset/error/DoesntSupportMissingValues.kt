package maia.ml.dataset.error

import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.type.DataType
import maia.util.eval

/**
 * Exception thrown when trying to clear the value from a column which
 * doesn't support missing values.
 *
 * @param header
 *          The header of the column.
 */
class DoesntSupportMissingValues(
    columnName: String? = null
): Exception(
    eval {
        val columnString = if (columnName != null)
            " \"${columnName}\""
        else
            ""
        "Column$columnString doesn't support missing values"
    }
) {
    constructor(header: DataColumnHeader): this("${header.name} @ #${header.index}")
}

/**
 * Throws the [DoesntSupportMissingValues] exception if the receiving
 * [DataType] doesn't support missing values.
 *
 * @receiver The [DataType] to check for missing-value support.
 */
fun DataType<*, *>.checkMissingValueSupport() {
    if (!supportsMissingValues)
        throw DoesntSupportMissingValues(header)
}

/**
 * Throws the [DoesntSupportMissingValues] exception if the receiving
 * [DataType] doesn't support missing values.
 *
 * @receiver The [DataType] to check for missing-value support.
 */
fun DataColumnHeader.checkMissingValueSupport() {
    if (!type.supportsMissingValues)
        throw DoesntSupportMissingValues(this)
}
