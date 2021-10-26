package māia.ml.dataset.view

import māia.ml.dataset.DataBatch
import māia.ml.dataset.DataRow
import māia.ml.dataset.headers.DataColumnHeaders
import māia.ml.dataset.type.DataRepresentation

/**
 * A read-only view of a single row of a data-batch.
 *
 * @param source    The data-set to source values from.
 * @param rowIndex  The row to view.
 */
open class DataBatchRowView(
        protected val source : DataBatch<*>,
        protected val rowIndex : Int
) : DataRow {
    override val headers get() = source.headers
    override fun <T> getValue(representation : DataRepresentation<*, *, out T>) : T = source.getValue(representation, rowIndex)
}

/**
 * Creates a read-only view of a single row of a data-batch.
 *
 * @receiver        The data-set to source values from.
 * @param rowIndex  The row to view.
 * @return          The view.
 */
fun DataBatch<*>.readOnlyViewRow(rowIndex : Int) : DataBatchRowView {
    return DataBatchRowView(this, rowIndex)
}
