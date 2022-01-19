package maia.ml.dataset.view

import maia.util.datastructure.OrderedHashSet
import maia.util.datastructure.OrderedSet
import maia.ml.dataset.DataRow
import maia.ml.dataset.headers.DataColumnHeadersView
import maia.ml.dataset.headers.MutableDataColumnHeadersBase
import maia.ml.dataset.headers.MutableDataColumnHeadersReadOnlyView
import maia.ml.dataset.type.DataRepresentation

/**
 * A read-only view of a sub-set of a data-row.
 *
 * @param source    The data-row to use as the source of values.
 * @param columns   The columns of the row to view, or null for all columns.
 */
open class DataRowView(
    source : DataRow,
    protected val columns : OrderedSet<Int>? = null
) : DataRow {

    constructor(source : DataRow, columns : Iterator<Int>) : this(source, OrderedHashSet<Int>().also { set ->
        columns.forEach { set.add(it) }
    })

    constructor(source : DataRow, vararg columns : Int) : this(source, columns.iterator())

    protected open val source = source

    override val headers = DataColumnHeadersView(source.headers, columns)

    override fun <T> getValue(representation : DataRepresentation<*, *, out T>) : T {
        return source.getValue(representation)
    }
}

/**
 * TODO
 */
fun DataRow.readOnlyView() : DataRowView {
    return DataRowView(this)
}

/**
 * TODO
 */
fun DataRow.readOnlyViewColumns(columns : OrderedSet<Int>) : DataRowView {
    return DataRowView(this, columns)
}
