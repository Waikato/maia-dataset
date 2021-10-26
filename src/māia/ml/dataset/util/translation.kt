package māia.ml.dataset.util

/*
 * Provides utilities for doing index-translation for views
 * which limit access to a sub-set of their source's rows/columns.
 */

import māia.ml.dataset.headers.DataColumnHeaders
import māia.util.datastructure.MutableOrderedSet
import māia.util.datastructure.OrderedSet
import māia.util.datastructure.buildOrderedSet
import māia.util.ensureIndexInRange
import māia.util.inlineRangeForLoop

/**
 * Uses the provided column translation set to translate a source column
 * index into a target column index.
 *
 * @param columns       An ordered set of column mappings. If null, no
 *                      mapping is performed.
 * @param columnIndex   The source column index.
 * @return              The target column index.
 */
fun translateColumn(columns : OrderedSet<Int>?, columnIndex : Int) : Int {
    return columns?.get(columnIndex) ?: columnIndex
}

/**
 * Uses the provided row translation list to translate a source row
 * index into a target row index.
 *
 * @param rows      An list of row mappings. If null, no
 *                  mapping is performed.
 * @param rowIndex  The source row index.
 * @return          The target row index.
 */
fun translateRow(rows : List<Int>?, rowIndex : Int) : Int {
    return rows?.get(rowIndex) ?: rowIndex
}

/**
 * TODO
 */
inline fun DataColumnHeaders.allColumnsExcept(
    index: Int
): MutableOrderedSet<Int> = ensureIndexInRange(index, size) {
    return buildOrderedSet {
        inlineRangeForLoop(this@allColumnsExcept.size) {
            if (it != index) add(it)
        }
    }
}
