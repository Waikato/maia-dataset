package maia.ml.dataset.view

import maia.ml.dataset.mutable.MutableDataBatch
import maia.ml.dataset.mutable.MutableDataColumn
import maia.ml.dataset.type.DataRepresentation

/**
 * A mutable view of a single column of a data-batch.
 *
 * TODO: Reinstate.
 *
 * @param source        The data-set to source values from.
 * @param columnIndex   The column to view.
 *
class MutableDataBatchColumnView<T>(
        source : MutableDataBatch<*>,
        representation : DataRepresentation<*, *, T>
) : DataBatchColumnView<T>(source, representation), MutableDataColumn<T> {
    
    override val source: MutableDataBatch<*> = source
    override val representation = representation
    
    override fun setRow(rowIndex : Int, value : T) {
        source.setValue(representation, rowIndex, value)
    }

    override fun setRows(rowIndex : Int, values : Collection<T>) {
        values.forEachIndexed { index, value -> setRow(rowIndex + index, value) }
    }

    override fun clearRow(rowIndex : Int) {
        source.clearValues(header.index)
    }

    override fun clearRows(rowIndex : Int, count : Int) {
        TODO("Not yet implemented")
    }


}

/**
 * TODO
 */
fun MutableDataBatch<*, *>.mutableViewColumn(columnIndex : Int) : MutableDataBatchColumnView {
    return MutableDataBatchColumnView(this, columnIndex)
}
*/
