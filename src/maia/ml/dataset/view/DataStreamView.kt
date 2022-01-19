package maia.ml.dataset.view

import maia.util.datastructure.OrderedHashSet
import maia.util.datastructure.OrderedSet
import maia.util.map
import maia.ml.dataset.DataMetadata
import maia.ml.dataset.DataRow
import maia.ml.dataset.DataStream
import maia.ml.dataset.headers.DataColumnHeadersView
import maia.ml.dataset.mutable.MutableDataRow

/**
 * A read-only view of a sub-set of columns from a data-stream.
 *
 * @param source    The data-stream to source rows from.
 * @param columns   The sub-set of columns to view, or null for all columns.
 */
open class DataStreamView(
    source : DataStream<*>,
    protected val columns : OrderedSet<Int>? = null
) : DataStream<DataRowView> {

    constructor(
        source : DataStream<*>,
        columns : Iterator<Int>
    ) : this(
        source,
        OrderedHashSet<Int>().also { set ->
            columns.forEach { set.add(it) }
        } as OrderedSet<Int>?
    )

    constructor(source : DataStream<*>, vararg columns : Int) : this(source, columns.iterator())

    protected open val source = source

    override val metadata: DataMetadata
        get() = source.metadata

    override val headers = DataColumnHeadersView(source.headers, columns)

    override val numColumns: Int = headers.size

    override fun rowIterator(): Iterator<DataRowView> {
        return source.rowIterator().map(this::wrapRow)
    }

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
