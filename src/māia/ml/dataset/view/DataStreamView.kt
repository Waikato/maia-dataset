package māia.ml.dataset.view

import māia.util.datastructure.OrderedHashSet
import māia.util.datastructure.OrderedSet
import māia.util.map
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataMetadata
import māia.ml.dataset.DataRow
import māia.ml.dataset.DataStream
import māia.ml.dataset.mutable.MutableDataRow
import māia.ml.dataset.util.translateColumn

/**
 * A read-only view of a sub-set of columns from a data-stream.
 *
 * @param source    The data-stream to source rows from.
 * @param columns   The sub-set of columns to view, or null for all columns.
 */
open class DataStreamView(
        protected val source : DataStream<*>,
        protected val columns : OrderedSet<Int>? = null
) : DataStream<DataRowView> {

    constructor(source : DataStream<*>, columns : Iterator<Int>) : this(source, OrderedHashSet<Int>().also { set ->
        columns.forEach { set.add(it) }
    } as OrderedSet<Int>?)

    constructor(source : DataStream<*>, vararg columns : Int) : this(source, columns.iterator())

    override val metadata: DataMetadata
        get() = source.metadata

    override fun rowIterator(): Iterator<DataRowView> {
        return source.rowIterator().map(this::wrapRow)
    }

    override val numColumns: Int
        get() = columns?.size ?: source.numColumns

    override fun getColumnHeader(columnIndex: Int): DataColumnHeader = source.getColumnHeader(
        translateColumn(columns, columnIndex)
    )

    protected fun wrapRow(sourceRow : DataRow) : DataRowView {
        return if (sourceRow is MutableDataRow)
            MutableDataRowView(sourceRow, columns)
        else
            DataRowView(sourceRow, columns)
    }
}

fun DataStream<*>.readOnlyView() : DataStreamView {
    return DataStreamView(this)
}

fun DataStream<*>.readOnlyViewColumns(columns : OrderedSet<Int>) : DataStreamView {
    return DataStreamView(this, columns)
}
