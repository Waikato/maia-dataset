package maia.ml.dataset.view

import kotlinx.coroutines.flow.Flow
import maia.util.datastructure.OrderedHashSet
import maia.util.datastructure.OrderedSet
import maia.util.map
import maia.ml.dataset.DataStream

/**
 * A read-only view of a sub-set of columns from a data-stream.
 *
 * @param source    The data-stream to source rows from.
 * @param columns   The sub-set of columns to view, or null for all columns.
 */
open class DataStreamView(
    source : DataStream<*>,
    columns : OrderedSet<Int>? = null
) : AsyncDataStreamView(source, columns), DataStream<DataRowView> {

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

    override val source = source

    override fun rowIterator(): Iterator<DataRowView> {
        return source.rowIterator().map(this::wrapRow)
    }

    override fun rowFlow() : Flow<DataRowView> {
        return super<AsyncDataStreamView>.rowFlow()
    }
}

fun DataStream<*>.readOnlyView() : DataStreamView {
    return DataStreamView(this)
}

fun DataStream<*>.readOnlyViewColumns(columns : OrderedSet<Int>) : DataStreamView {
    return DataStreamView(this, columns)
}
