package maia.ml.dataset.view

import maia.util.map
import maia.ml.dataset.DataColumn
import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.util.translateRow

/**
 * A read-only view of a data-column.
 *
 * @param source    The source data-column to view.
 * @param rows      The rows of the data-column to view, or null for all rows.
 */
open class DataColumnView<out T>(
        source : DataColumn<T>,
        protected val rows: List<Int>? = null
) : DataColumn<T> {

    protected open val source = source

    override val header : DataColumnHeader get() = source.header

    override val numRows : Int
        get() = rows?.size ?: source.numRows

    override fun rowIterator() : Iterator<T> {
        if (rows == null)
            return source.rowIterator()

        return rows.iterator().map { source.getRow(it) }
    }

    override fun getRow(rowIndex : Int) : T = source.getRow(translateRow(rows, rowIndex))

}

/**
 * TODO
 */
fun <T> DataColumn<T>.readOnlyView() : DataColumnView<T> {
    return DataColumnView(this)
}

/**
 * TODO
 */
fun <T> DataColumn<T>.readOnlyViewRows(rows: List<Int>) : DataColumnView<T> {
    return DataColumnView(this, rows)
}
