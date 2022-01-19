package maia.ml.dataset.view

/**
 * A mutable view of a single row of a data-batch.
 *
 * TODO: Reinstate.
 *
 * @param source    The data-set to source values from.
 * @param rowIndex  The row to view.
 *
class MutableDataBatchRowView(
        source : MutableDataBatch<*, *>,
        rowIndex : Int
) : DataBatchRowView(source, rowIndex), MutableDataRow {

    override fun setColumn(columnIndex : Int, column : Any?) = (source as MutableDataBatch<*, *>).setValue(rowIndex, columnIndex, column)

}

/**
 * TODO
 */
fun MutableDataBatch<*, *>.mutableViewRow(rowIndex : Int) : MutableDataBatchRowView {
    return MutableDataBatchRowView(this, rowIndex)
}
*/
