package māia.ml.dataset.util

import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataRow

/**
 * Iterates over all columns of a data-row.
 *
 * @receiver
 *          The data-row to iterate over.
 * @param block
 *          The action to perform on each column.
 */
inline fun DataRow.forEachColumn(
    start: Int = 0,
    end: Int = numColumns,
    block: (Int, DataColumnHeader, Any?) -> Unit
) {
    for (columnIndex in start until end) {
        block(
            columnIndex,
            getColumnHeader(columnIndex),
            getColumn(columnIndex)
        )
    }
}
