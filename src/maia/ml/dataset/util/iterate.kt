package maia.ml.dataset.util

import maia.ml.dataset.WithColumns
import maia.ml.dataset.headers.header.DataColumnHeader

/**
 * Iterates over all columns of a data-structure.
 *
 * @receiver
 *          The data-structure to iterate over.
 * @param block
 *          The action to perform on each column.
 */
inline fun WithColumns.forEachColumn(
    start: Int = 0,
    end: Int = numColumns,
    block: (Int, DataColumnHeader) -> Unit
) {
    for (columnIndex in start until end) {
        block(
            columnIndex,
            headers[columnIndex]
        )
    }
}
