package māia.ml.dataset.view

import māia.util.datastructure.OrderedSet
import māia.ml.dataset.DataColumn
import māia.ml.dataset.DataRow
import māia.ml.dataset.mutable.MutableDataBatch
import māia.ml.dataset.util.clone
import māia.ml.dataset.util.translateColumn
import māia.ml.dataset.util.translateRow

/**
 * A mutable view of a sub-set of rows/columns from a data-batch.
 *
 * @param source    The data-batch to source values from.
 * @param columns   The sub-set of columns to view, or null for all columns.
 * @param rows      The sub-set of rows to view, or null for all rows.
 */
open class MutableDataBatchView(
        source : MutableDataBatch<*, *>,
        columns : OrderedSet<Int>? = null,
        rows : List<Int>? = null
) : DataBatchView(source, columns, rows), MutableDataBatch<DataRowView, DataColumnView> {

    override val castSource : MutableDataBatch<*, *>
        get() = source as MutableDataBatch<*, *>

    override fun setValue(rowIndex : Int, columnIndex : Int, value : Any?) {
        castSource.setValue(
                translateRow(rows, rowIndex),
                translateColumn(columns, columnIndex),
                value
        )
    }

    override fun setRow(rowIndex : Int, value : DataRow) {
        MutableDataBatchRowView(this, rowIndex).clone(value)
    }

    override fun setColumn(columnIndex : Int, column : DataColumn) {
        MutableDataBatchColumnView(this, columnIndex).clone(column)
    }

}

/**
 * Creates a read-only view of a data-batch.
 *
 * TODO
 */
fun MutableDataBatch<*, *>.mutableView() : MutableDataBatchView {
    return MutableDataBatchView(this)
}

/**
 * TODO
 */
fun MutableDataBatch<*, *>.mutableView(rows : List<Int>, columns : OrderedSet<Int>) : MutableDataBatchView {
    return MutableDataBatchView(this, columns, rows)
}

/**
 * TODO
 */
fun MutableDataBatch<*, *>.mutableViewColumns(columns : OrderedSet<Int>) : MutableDataBatchView {
    return MutableDataBatchView(this, columns)
}

/**
 * TODO
 */
fun MutableDataBatch<*, *>.mutableViewRows(rows : List<Int>) : MutableDataBatchView {
    return MutableDataBatchView(this, null, rows)
}
