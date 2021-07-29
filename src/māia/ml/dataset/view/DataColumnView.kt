package māia.ml.dataset.view

import māia.util.map
import māia.ml.dataset.DataColumn
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.util.translateRow

/**
 * A read-only view of a data-column.
 *
 * @param source    The source data-column to view.
 * @param rows      The rows of the data-column to view, or null for all rows.
 */
open class DataColumnView(
        protected val source : DataColumn,
        protected val rows: List<Int>? = null
) : DataColumn {

    override val header : DataColumnHeader
        get() = source.header

    override val numRows : Int
        get() = rows?.size ?: source.numRows

    override fun rowIterator() : Iterator<Any?> {
        if (rows == null)
            return source.rowIterator()

        return rows
                .iterator()
                .map { source.getRow(it) }
    }

    override fun getRow(rowIndex : Int) : Any? {
        return source.getRow(translateRow(rows, rowIndex))
    }

}

/**
 * TODO
 */
fun DataColumn.readOnlyView() : DataColumnView {
    return DataColumnView(this)
}

/**
 * TODO
 */
fun DataColumn.readOnlyViewRows(rows: List<Int>) : DataColumnView {
    return DataColumnView(this, rows)
}
