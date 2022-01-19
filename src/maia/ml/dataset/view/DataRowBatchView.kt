package maia.ml.dataset.view

import maia.ml.dataset.DataBatch
import maia.ml.dataset.DataColumn
import maia.ml.dataset.DataMetadata
import maia.ml.dataset.DataRow
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.util.DummyMetadata
import maia.util.ensureIndexInRange
import maia.util.itemIterator

/**
 * A read-only view of a single row as a data-batch.
 *
 * @param source    The data-row to use as the source.
 * @param metadata  The meta-data to give the data-batch view.
 * @param R         The type of data-row being viewed.
 */
open class DataRowBatchView<out R : DataRow>(
        protected val source : R,
        override val metadata : DataMetadata = DummyMetadata
) : DataBatch<R> {
    override val headers get() = source.headers
    override val numRows : Int = 1
    override val numColumns : Int get() = source.numColumns
    override fun getRow(rowIndex : Int) : R = ensureIndexInRange(rowIndex, 1) { source }
    override fun rowIterator() : Iterator<R> = itemIterator(source)
    override fun <T> getColumn(representation : DataRepresentation<*, *, T>) : DataColumn<T> = DataRowColumnView(source, representation)
    override fun <T> getValue(representation : DataRepresentation<*, *, out T>, rowIndex : Int) : T = ensureIndexInRange(rowIndex, 1) { source.getValue(representation) }
}

/**
 * TODO
 */
fun <R : DataRow> R.viewAsDataBatch() : DataRowBatchView<R> {
    return DataRowBatchView(this)
}

/**
 * TODO
 */
fun <R : DataRow> R.viewAsDataBatch(metadata : DataMetadata) : DataRowBatchView<R> {
    return DataRowBatchView(this, metadata)
}
