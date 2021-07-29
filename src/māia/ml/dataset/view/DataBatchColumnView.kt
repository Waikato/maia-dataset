package māia.ml.dataset.view

import māia.ml.dataset.DataBatch
import māia.ml.dataset.DataColumn
import māia.ml.dataset.DataColumnHeader

/**
 * A read-only view of a single column of a data-batch.
 *
 * @param source        The data-set to source values from.
 * @param columnIndex   The column to view.
 */
open class DataBatchColumnView(
        protected val source : DataBatch<*, *>,
        protected val columnIndex : Int
) : DataColumn {

    override val header : DataColumnHeader
        get() = source.getColumnHeader(columnIndex)

    override val numRows : Int
        get() = source.numRows

    override fun getRow(rowIndex : Int) : Any? {
        return source.getValue(rowIndex, columnIndex)
    }

}

/**
 * Creates a read-only view of a single column of a data-batch.
 *
 * @receiver            The data-set to source values from.
 * @param columnIndex   The column to view.
 * @return              The view.
 */
fun DataBatch<*, *>.readOnlyViewColumn(columnIndex : Int) : DataBatchColumnView {
    return DataBatchColumnView(this, columnIndex)
}
