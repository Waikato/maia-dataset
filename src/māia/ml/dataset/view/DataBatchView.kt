package māia.ml.dataset.view

import māia.util.*
import māia.util.datastructure.OrderedSet
import māia.ml.dataset.*
import māia.ml.dataset.mutable.MutableDataColumn
import māia.ml.dataset.util.translateColumn
import māia.ml.dataset.util.translateRow

/**
 * A read-only view of a sub-set of rows/columns from a data-batch.
 *
 * @param source    The data-batch to source values from.
 * @param columns   The sub-set of columns to view, or null for all columns.
 * @param rows      The sub-set of rows to view, or null for all rows.
 */
open class DataBatchView(
        source : DataBatch<*, *>,
        columns : OrderedSet<Int>? = null,
        protected val rows : List<Int>? = null
)
    : DataStreamView(source, columns),
        DataBatch<DataRowView, DataColumnView> {

    /** The source cast to its actual type. */
    protected open val castSource : DataBatch<*, *>
        get() = source as DataBatch<*, *>

    override val metadata: DataMetadata
        get() = source.metadata

    override val numRows : Int
        get() = rows?.size ?: castSource.numRows

    override fun getRow(rowIndex : Int) : DataRowView = wrapRow(castSource.getRow(translateRow(rows, rowIndex)))

    override fun getColumn(columnIndex : Int) : DataColumnView = wrapColumn(castSource.getColumn(translateColumn(columns, columnIndex)))

    override fun getValue(rowIndex : Int, columnIndex : Int) : Any? = castSource.getValue(translateRow(rows, rowIndex), translateColumn(columns, columnIndex))

    override fun rowIterator(): Iterator<DataRowView> = (0 until numRows).iterator().map { getRow(it) }

    override fun getColumnHeader(columnIndex: Int): DataColumnHeader = source.getColumnHeader(translateColumn(columns, columnIndex))

    /**
     * TODO
     */
    protected fun wrapColumn(sourceColumn : DataColumn) : DataColumnView {
        return if (sourceColumn is MutableDataColumn)
            MutableDataColumnView(sourceColumn, rows)
        else
            DataColumnView(sourceColumn, rows)
    }
}

/**
 * Creates a read-only view of a data-batch.
 *
 * TODO
 */
fun DataBatch<*, *>.readOnlyView() : DataBatchView {
    return DataBatchView(this)
}

/**
 * TODO
 */
fun DataBatch<*, *>.readOnlyView(rows : List<Int>, columns : OrderedSet<Int>) : DataBatchView {
    return DataBatchView(this, columns, rows)
}

/**
 * TODO
 */
fun DataBatch<*, *>.readOnlyViewColumns(columns : OrderedSet<Int>) : DataBatchView {
    return DataBatchView(this, columns)
}

/**
 * TODO
 */
fun DataBatch<*, *>.readOnlyViewRows(rows : List<Int>) : DataBatchView {
    return DataBatchView(this, null, rows)
}
