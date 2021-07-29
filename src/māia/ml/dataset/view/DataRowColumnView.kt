package māia.ml.dataset.view

import māia.ml.dataset.DataColumn
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataRow

/**
 * A read-only view of a single column from a row as
 * a data-column of size one.
 *
 * @param source        The source data-row.
 * @param columnIndex   The column of the row to view.
 */
open class DataRowColumnView(
        protected val source : DataRow,
        protected val columnIndex : Int
) : DataColumn {

    override val numRows : Int = 1

    override val header : DataColumnHeader
        get() = source.getColumnHeader(columnIndex)

    override fun getRow(rowIndex : Int) : Any? {
        return source.getColumn(columnIndex)
    }

}

/**
 * TODO
 */
fun DataRow.readOnlyViewColumn(columnIndex : Int) : DataRowColumnView {
    return DataRowColumnView(this, columnIndex)
}
