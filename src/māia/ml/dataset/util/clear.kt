package māia.ml.dataset.util

/**
 * Methods for clearing mutable structures.
 */

import māia.ml.dataset.mutable.WithMutableColumnStructure
import māia.ml.dataset.mutable.WithMutableRowStructure

/**
 * Removes all columns from a data-structure with mutable
 * column structure.
 */
fun WithMutableColumnStructure<*, *>.clearColumns() {
    while (numColumns > 0)
        deleteColumn(0)
}

/**
 * Removes all rows from data-structures with mutable
 * row structure.
 */
fun WithMutableRowStructure<*, *>.clearRows() {
    while (numRows > 0)
        deleteRow(0)
}

/**
 * Removes all rows and columns from data-structures with mutable
 * row/column structure.
 */
fun <T> T.clear()
where T : WithMutableColumnStructure<*, *>, T : WithMutableRowStructure<*, *> {
    clearRows()
    clearColumns()
}
