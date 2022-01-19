package maia.ml.dataset.view

import maia.ml.dataset.DataBatch
import maia.ml.dataset.DataColumn
import maia.ml.dataset.headers.ensureOwnership
import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.headers.header.DataColumnHeaderView
import maia.ml.dataset.type.DataRepresentation

/**
 * A read-only view of a single column of a data-batch.
 *
 * @param source        The data-set to source values from.
 * @param columnIndex   The column to view.
 */
class DataBatchColumnView<T>(
    private val source : DataBatch<*>,
    representation: DataRepresentation<*, *, out T>
) : DataColumn<T> {

    /**
     * The representational-type of the column (pre-translated into the owned
     * equivalent for the source.
     */
    private val representation = source.headers.ensureOwnership(representation) { this }

    override val header : DataColumnHeader =
        DataColumnHeaderView(this.representation.dataType.header)

    override val numRows : Int
        get() = source.numRows

    override fun getRow(rowIndex : Int) : T = source.getValue(representation, rowIndex)

}

/**
 * Creates a read-only view of a single column of a data-batch.
 *
 * @receiver            The data-set to source values from.
 * @param columnIndex   The column to view.
 * @return              The view.
 */
fun <T> DataBatch<*>.readOnlyViewColumn(
    representation: DataRepresentation<*, *, out T>
) : DataBatchColumnView<T> {
    return DataBatchColumnView(this, representation)
}
