package māia.ml.dataset.view

import māia.ml.dataset.mutable.MutableDataColumn
import māia.ml.dataset.util.translateRow

/**
 * A mutable view of a data-column.
 *
 * @param source    The source data-column to view.
 * @param rows      The rows of the data-column to view, or null for all rows.
 */
class MutableDataColumnView(
        source : MutableDataColumn,
        rows: List<Int>? = null
) : DataColumnView(source, rows), MutableDataColumn {

    override fun setRow(rowIndex : Int, value : Any?) = (source as MutableDataColumn).setRow(translateRow(rows, rowIndex), value)

}

/**
 * TODO
 */
fun MutableDataColumn.readOnlyView() : MutableDataColumnView {
    return MutableDataColumnView(this)
}

/**
 * TODO
 */
fun MutableDataColumn.readOnlyViewRows(rows: List<Int>) : MutableDataColumnView {
    return MutableDataColumnView(this, rows)
}
