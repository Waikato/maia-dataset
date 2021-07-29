package māia.ml.dataset.view

import māia.ml.dataset.DataBatch
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataMetadata
import māia.ml.dataset.DataRow
import māia.ml.dataset.util.DummyMetadata
import māia.util.itemIterator

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
) : DataBatch<R, DataRowColumnView>{

    override val numRows : Int = 1

    override val numColumns : Int
        get() = source.numColumns

    override fun getValue(rowIndex : Int, columnIndex : Int) : Any? {
        if (rowIndex != 0) throw IndexOutOfBoundsException(rowIndex)
        return source.getColumn(columnIndex)
    }

    override fun getColumn(columnIndex : Int) : DataRowColumnView {
        return DataRowColumnView(source, columnIndex)
    }

    override fun getRow(rowIndex : Int) : R {
        return source
    }

    override fun getColumnHeader(columnIndex : Int) : DataColumnHeader {
        return source.getColumnHeader(columnIndex)
    }

    override fun rowIterator() : Iterator<R> {
        return itemIterator(source)
    }
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
