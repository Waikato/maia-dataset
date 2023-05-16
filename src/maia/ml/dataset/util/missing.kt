package maia.ml.dataset.util

import maia.ml.dataset.DataBatch
import maia.ml.dataset.DataRow
import maia.ml.dataset.error.MissingValue
import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.type.*


// region DataRow

/**
 * Whether the value of a data-row at the given column is missing or not.
 *
 * @receiver
 *          The data-row to check for a missing value.
 * @param representation
 *          The representation of the column to check for a missing value.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataRow.isMissing(representation : DataRepresentation<*, *, *>): Boolean {
    ifNotMissing(representation) {
        return false
    }
    return true
}

/**
 * Whether the value of a data-row at the given column is missing or not.
 *
 * @receiver
 *          The data-row to check for a missing value.
 * @param type
 *          The data-type of the column to check for a missing value.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataRow.isMissing(type : DataType<*, *>): Boolean {
    return isMissing(type.canonicalRepresentation)
}

/**
 * Whether the value of a data-row at the given column is missing or not.
 *
 * @receiver
 *          The data-row to check for a missing value.
 * @param header
 *          The header of the column to check for a missing value.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataRow.isMissing(header : DataColumnHeader): Boolean {
    return isMissing(header.type)
}

/**
 * Whether the value of a data-row at the given column is missing or not.
 *
 * @receiver
 *          The data-row to check for a missing value.
 * @param name
 *          The name of the column to check for a missing value.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataRow.isMissing(name: String): Boolean {
    return isMissing(headers[name]
        ?: throw IllegalArgumentException("Unknown header '$name'"))
}

/**
 * Whether the value of a data-row at the given column is missing or not.
 *
 * @receiver
 *          The data-row to check for a missing value.
 * @param index
 *          The index of the column to check for a missing value.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataRow.isMissing(index: Int): Boolean {
    return isMissing(headers[index])
}

/**
 * Performs the given action if the value of a data-row at a certain
 * column is not missing.
 *
 * @receiver
 *          The data-row to check for a missing value.
 * @param representation
 *          The column representation to check for a missing value.
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

/**
 * Gets the value of a data-row at a certain column, or calculates a
 * default value if it is missing.
 *
 * @receiver
 *          The data-row to get the value from, if present.
 * @param representation
 *          The column representation to get the value from, if present.
 * @param block
 *          Calculates a default value if the value is missing.
 * @return
 *          The value if it was present, or the calculated default if not.
 */
inline fun <T> DataRow.getValueIfNotMissingOrElse(
    representation : DataRepresentation<*, *, T>,
    block: () -> T
): T {
    ifNotMissing(representation) { return it }

    return block()
}

/**
 * Gets the value of a data-row at a certain column, or returns a
 * default value if it is missing.
 *
 * @receiver
 *          The data-row to get the value from, if present.
 * @param representation
 *          The column representation to get the value from, if present.
 * @param default
 *          The default value to return if the value is missing.
 * @return
 *          The value if it was present, or the default if not.
 */
inline fun <T> DataRow.getValueIfNotMissingOrDefault(
    representation : DataRepresentation<*, *, T>,
    default: T
): T {
    return getValueIfNotMissingOrElse(representation) { default }
}

// endregion

// region DataBatch

/**
 * Whether the value of a data-batch at the given column/row is missing or not.
 *
 * @receiver
 *          The data-batch to check for a missing value.
 * @param representation
 *          The representation of the column to check for a missing value.
 * @param rowIndex
 *          The index of the row to check.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataBatch<*>.isMissing(
    representation : DataRepresentation<*, *, *>,
    rowIndex: Int
): Boolean {
    ifNotMissing(representation, rowIndex) {
        return false
    }
    return true
}

/**
 * Whether the value of a data-batch at the given column/row is missing or not.
 *
 * @receiver
 *          The data-batch to check for a missing value.
 * @param type
 *          The data-type of the column to check for a missing value.
 * @param rowIndex
 *          The index of the row to check.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataBatch<*>.isMissing(
    type : DataType<*, *>,
    rowIndex: Int
): Boolean {
    return isMissing(type.canonicalRepresentation, rowIndex)
}

/**
 * Whether the value of a data-batch at the given column/row is missing or not.
 *
 * @receiver
 *          The data-batch to check for a missing value.
 * @param header
 *          The header of the column to check for a missing value.
 * @param rowIndex
 *          The index of the row to check.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataBatch<*>.isMissing(
    header : DataColumnHeader,
    rowIndex: Int
): Boolean {
    return isMissing(header.type, rowIndex)
}

/**
 * Whether the value of a data-batch at the given column/row is missing or not.
 *
 * @receiver
 *          The data-batch to check for a missing value.
 * @param name
 *          The name of the column to check for a missing value.
 * @param rowIndex
 *          The index of the row to check.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataBatch<*>.isMissing(
    name: String,
    rowIndex: Int
): Boolean {
    return isMissing(
        headers[name] ?: throw IllegalArgumentException("Unknown header '$name'"),
        rowIndex
    )
}

/**
 * Whether the value of a data-batch at the given column/row is missing or not.
 *
 * @receiver
 *          The data-batch to check for a missing value.
 * @param index
 *          The index of the column to check for a missing value.
 * @param rowIndex
 *          The index of the row to check.
 * @return
 *          Whether the value is missing or not.
 */
inline fun DataBatch<*>.isMissing(
    index: Int,
    rowIndex: Int
): Boolean {
    return isMissing(headers[index], rowIndex)
}

/**
 * Performs the given action if the value of a data-batch at a certain
 * column/row is not missing.
 *
 * @receiver
 *          The data-batch to check for a missing value.
 * @param representation
 *          The representation of the column to check for a missing value.
 * @param rowIndex
 *          The index of the row to check.
 * @param block
 *          The action to take if the value is present.
 */
inline fun <T> DataBatch<*>.ifNotMissing(
    representation : DataRepresentation<*, *, T>,
    rowIndex: Int,
    block: (T) -> Unit
) {
    val value = try {
        getValue(representation, rowIndex)
    } catch (e: MissingValue) {
        return
    }

    block(value)
}

/**
 * Gets the value of a data-batch at a certain column/row, or calculates a
 * default value if it is missing.
 *
 * @receiver
 *          The data-batch to get the value from, if present.
 * @param representation
 *          The column representation to get the value from, if present.
 * @param rowIndex
 *          The index of the row to check.
 * @param block
 *          Calculates a default value if the value is missing.
 * @return
 *          The value if it was present, or the calculated default if not.
 */
inline fun <T> DataBatch<*>.getValueIfNotMissingOrElse(
    representation : DataRepresentation<*, *, T>,
    rowIndex: Int,
    block: () -> T
): T {
    ifNotMissing(representation, rowIndex) { return it }

    return block()
}

/**
 * Gets the value of a data-batch at a certain column/row, or returns a
 * default value if it is missing.
 *
 * @receiver
 *          The data-batch to get the value from, if present.
 * @param representation
 *          The column representation to get the value from, if present.
 * @param rowIndex
 *          The index of the row to check.
 * @param default
 *          The default value to return if the value is missing.
 * @return
 *          The value if it was present, or the default if not.
 */
inline fun <T> DataBatch<*>.getValueIfNotMissingOrDefault(
    representation : DataRepresentation<*, *, T>,
    rowIndex: Int,
    default: T
): T {
    return getValueIfNotMissingOrElse(representation, rowIndex) { default }
}

// endregion
