package maia.ml.dataset.util

import maia.ml.dataset.DataRow
import maia.ml.dataset.headers.header.DataColumnHeader

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
    end: Int = headers.size,
    block: (Int, DataColumnHeader) -> Unit
) {
    for (columnIndex in start until end) {
        block(
            columnIndex,
            headers[columnIndex]
        )
    }
}
