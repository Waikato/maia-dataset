package maia.ml.dataset.view

import maia.util.datastructure.OrderedSet
import maia.ml.dataset.*
import maia.ml.dataset.headers.ensureOwnership
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.util.translateRow

/**
 * A read-only view of a sub-set of rows/columns from a data-batch.
 *
 * @param source    The data-batch to source values from.
 * @param columns   The sub-set of columns to view, or null for all columns.
 * @param rows      The sub-set of rows to view, or null for all rows.
 */
open class DataBatchView(
    source : DataBatch<*>,
    columns : OrderedSet<Int>? = null,
    protected val rows : List<Int>? = null
) : DataStreamView(source, columns), DataBatch<DataRowView> {

    override fun <T> getColumn(representation : DataRepresentation<*, *, T>) : DataColumn<T> = DataBatchColumnView(source, representation)

    override fun <T> getValue(
        representation : DataRepresentation<*, *, out T>,
        rowIndex : Int
    ) : T  = headers.ensureOwnership(representation) {
        source.getValue(representation, translateRow(rows, rowIndex))
    }

    override val source : DataBatch<*> = source

    override val numRows : Int = rows?.size ?: source.numRows

    override fun getRow(rowIndex : Int) : DataRowView = wrapRow(source.getRow(translateRow(rows, rowIndex)))

    override fun rowIterator() : Iterator<DataRowView> = super<DataBatch>.rowIterator()
}

/**
 * Creates a read-only view of a data-batch.
 *
 * TODO
 */
fun DataBatch<*>.readOnlyView() : DataBatchView {
    return DataBatchView(this)
}

/**
 * TODO
 */
fun DataBatch<*>.readOnlyView(rows : List<Int>, columns : OrderedSet<Int>) : DataBatchView {
    return DataBatchView(this, columns, rows)
}

/**
 * TODO
 */
fun DataBatch<*>.readOnlyViewColumns(columns : OrderedSet<Int>) : DataBatchView {
    return DataBatchView(this, columns)
}

/**
 * TODO
 */
fun DataBatch<*>.readOnlyViewRows(rows : List<Int>) : DataBatchView {
    return DataBatchView(this, null, rows)
}
