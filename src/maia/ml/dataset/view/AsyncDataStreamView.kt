package maia.ml.dataset.view

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import maia.ml.dataset.AsyncDataStream
import maia.util.datastructure.OrderedHashSet
import maia.util.datastructure.OrderedSet
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
open class AsyncDataStreamView(
    source : AsyncDataStream<*>,
    protected val columns : OrderedSet<Int>? = null
) : AsyncDataStream<DataRowView> {

    constructor(
        source : AsyncDataStream<*>,
        columns : Iterator<Int>
    ) : this(
        source,
        OrderedHashSet<Int>().also { set ->
            columns.forEach { set.add(it) }
        } as OrderedSet<Int>?
    )

    constructor(source : AsyncDataStream<*>, vararg columns : Int) : this(source, columns.iterator())

    protected open val source = source

    override val metadata: DataMetadata
        get() = source.metadata

    override val headers = DataColumnHeadersView(source.headers, columns)

    override val numColumns: Int = headers.size

    override fun rowFlow() : Flow<DataRowView> {
        return source.rowFlow().map(this::wrapRow)
    }

    protected fun wrapRow(sourceRow : DataRow) : DataRowView {
        return if (sourceRow is MutableDataRow)
            MutableDataRowView(sourceRow, columns)
        else
            DataRowView(sourceRow, columns)
    }
}

fun AsyncDataStream<*>.readOnlyView() : AsyncDataStreamView {
    if (this is DataStream<*>) return this.readOnlyView()
    return AsyncDataStreamView(this)
}

fun AsyncDataStream<*>.readOnlyViewColumns(columns : OrderedSet<Int>) : AsyncDataStreamView {
    if (this is DataStream<*>) return this.readOnlyViewColumns(columns)
    return AsyncDataStreamView(this, columns)
}
