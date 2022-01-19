package maia.ml.dataset.util

/**
 * Methods for clearing mutable structures.
 */

import maia.ml.dataset.DataRow
import maia.ml.dataset.mutable.MutableColumnStructureDataBatch
import maia.ml.dataset.mutable.MutableColumnStructureDataRow
import maia.ml.dataset.mutable.WithMutableRowStructure


/**
 * Removes all rows from data-structures with mutable
 * row structure.
 */
fun WithMutableRowStructure<*, *>.clearRows() = deleteRows(0, numRows)

/**
 * Removes all rows and columns from a data-row.
 */
fun <T> T.clear()
where T : MutableColumnStructureDataRow, T : WithMutableRowStructure<*, *> {
    clearColumns()
    clearRows()
}

/**
 * Removes all rows and columns from a data-batch.
 */
fun <T, Rout> T.clear()
        where T : MutableColumnStructureDataBatch<Rout>, T : WithMutableRowStructure<DataRow, Rout> {
    clearColumns()
    clearRows()
}
