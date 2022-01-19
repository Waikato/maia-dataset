package maia.ml.dataset.view

import maia.util.datastructure.OrderedSet
import maia.ml.dataset.DataColumn
import maia.ml.dataset.DataRow
import maia.ml.dataset.mutable.MutableDataBatch
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.util.translateColumn
import maia.ml.dataset.util.translateRow

/**
 * A mutable view of a sub-set of rows/columns from a data-batch.
 *
 * TODO: Reinstate
 *
 * @param source    The data-batch to source values from.
 * @param columns   The sub-set of columns to view, or null for all columns.
 * @param rows      The sub-set of rows to view, or null for all rows.
 *
open class MutableDataBatchView(
        source : MutableDataBatch<*>,
        columns : OrderedSet<Int>? = null,
        rows : List<Int>? = null
) : DataBatchView(source, columns, rows), MutableDataBatch<DataRowView> {

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

    override fun <T> setColumn(
        representation : DataRepresentation<*, *, in T>,
        column : Collection<T>
    ) {
        TODO("Not yet implemented")
    }

    override fun clearColumn(columnIndex : Int) {
        TODO("Not yet implemented")
    }

    override fun <T> setValue(
        representation : DataRepresentation<*, *, in T>,
        rowIndex : Int,
        value : T
    ) {
        TODO("Not yet implemented")
    }

    override fun <T> setValues(
        representation : DataRepresentation<*, *, in T>,
        rowIndex : Int,
        values : Collection<T>
    ) {
        TODO("Not yet implemented")
    }

    override fun clearValue(columnIndex : Int, rowIndex : Int) {
        TODO("Not yet implemented")
    }

    override fun clearValues(columnIndex : Int, rowIndex : Int, count : Int) {
        TODO("Not yet implemented")
    }

    override fun setRows(rowIndex : Int, values : Collection<DataRow>) {
        TODO("Not yet implemented")
    }

    override fun clearRow(rowIndex : Int) {
        TODO("Not yet implemented")
    }

    override fun clearRows(rowIndex : Int, count : Int) {
        TODO("Not yet implemented")
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
*/
