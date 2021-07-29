package māia.ml.dataset.view

import māia.ml.dataset.mutable.MutableDataBatch
import māia.ml.dataset.mutable.MutableDataColumn

/**
 * A mutable view of a single column of a data-batch.
 *
 * @param source        The data-set to source values from.
 * @param columnIndex   The column to view.
 */
class MutableDataBatchColumnView(
        source : MutableDataBatch<*, *>,
        columnIndex : Int
) : DataBatchColumnView(source, columnIndex), MutableDataColumn {

    override fun setRow(rowIndex : Int, value : Any?) {
        (source as MutableDataBatch<*, *>).setValue(rowIndex, columnIndex, value)
    }

}

/**
 * TODO
 */
fun MutableDataBatch<*, *>.mutableViewColumn(columnIndex : Int) : MutableDataBatchColumnView {
    return MutableDataBatchColumnView(this, columnIndex)
}
