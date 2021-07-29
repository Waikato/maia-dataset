package māia.ml.dataset.view

import māia.ml.dataset.DataBatch
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataRow

/**
 * A read-only view of a single row of a data-batch.
 *
 * @param source    The data-set to source values from.
 * @param rowIndex  The row to view.
 */
open class DataBatchRowView(
        protected val source : DataBatch<*, *>,
        protected val rowIndex : Int
) : DataRow {

    override val numColumns : Int
        get() = source.numColumns

    override fun getColumn(columnIndex : Int) : Any? {
        return source.getValue(rowIndex, columnIndex)
    }

    override fun getColumnHeader(columnIndex : Int) : DataColumnHeader {
        return source.getColumnHeader(columnIndex)
    }

}

/**
 * Creates a read-only view of a single row of a data-batch.
 *
 * @receiver        The data-set to source values from.
 * @param rowIndex  The row to view.
 * @return          The view.
 */
fun DataBatch<*, *>.readOnlyViewRow(rowIndex : Int) : DataBatchRowView {
    return DataBatchRowView(this, rowIndex)
}
