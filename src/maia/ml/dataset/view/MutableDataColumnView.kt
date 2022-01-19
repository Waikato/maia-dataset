package maia.ml.dataset.view

import maia.ml.dataset.mutable.MutableDataColumn
import maia.ml.dataset.util.translateRow
import maia.util.ensureIndexInRange

/**
 * A mutable view of a data-column.
 *
 * @param source    The source data-column to view.
 * @param rows      The rows of the data-column to view, or null for all rows.
 */
class MutableDataColumnView<T>(
        source : MutableDataColumn<T>,
        rows: List<Int>? = null
) : DataColumnView<T>(source, rows), MutableDataColumn<T> {

    override val source: MutableDataColumn<T> = source

    override fun setRow(rowIndex : Int, value : T) = ensureIndexInRange(rowIndex, numRows) {
        source.setRow(translateRow(rows, rowIndex), value)
    }

    override fun setRows(rowIndex : Int, values : Collection<T>) = ensureIndexInRange(rowIndex, numRows) {
        ensureIndexInRange(rowIndex + values.size, numRows, true) {
            values.forEachIndexed { index, value ->
                source.setRow(translateRow(rows, rowIndex + index), value)
            }
        }
    }

    override fun clearRow(rowIndex : Int) = ensureIndexInRange(rowIndex, numRows) {
        source.clearRow(translateRow(rows, numRows))
    }

    override fun clearRows(rowIndex : Int, count : Int) = ensureIndexInRange(rowIndex, numRows) {
        ensureIndexInRange(rowIndex + count, numRows, true) {
            for (index in rowIndex until rowIndex + count) {
                source.clearRow(translateRow(rows, index))
            }
        }
    }


}

/**
 * TODO
 */
fun <T> MutableDataColumn<T>.readOnlyView() : MutableDataColumnView<T> {
    return MutableDataColumnView(this)
}

/**
 * TODO
 */
fun <T> MutableDataColumn<T>.readOnlyViewRows(rows: List<Int>) : MutableDataColumnView<T> {
    return MutableDataColumnView(this, rows)
}
