package māia.ml.dataset.view

import māia.util.datastructure.OrderedSet
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.WithColumnHeaders
import māia.ml.dataset.util.translateColumn
import māia.util.datastructure.OrderedHashSet
import māia.util.collect
import māia.util.filter
import māia.util.indexIterator

/**
 * Immutable view of a sub-set of the column-headers of another structure.
 *
 * @param source    The source of the column headers.
 * @param columns   The sub-set of columns to view, or null for all columns.
 */
open class WithColumnHeadersView(
        protected val source : WithColumnHeaders,
        protected val columns : OrderedSet<Int>? = null
) : WithColumnHeaders {

    override val numColumns : Int
        get() = columns?.size ?: source.numColumns

    override fun getColumnHeader(columnIndex : Int) : DataColumnHeader = source.getColumnHeader(translateColumn(columns, columnIndex))
}

/**
 * TODO
 */
fun WithColumnHeaders.readOnlyView() : WithColumnHeadersView {
    return WithColumnHeadersView(this)
}

/**
 * TODO
 */
fun WithColumnHeaders.readOnlyViewColumns(columns : OrderedSet<Int>) : WithColumnHeadersView {
    return WithColumnHeadersView(this, columns)
}

/**
 * TODO
 */
fun WithColumnHeaders.readOnlyViewAllColumnsExcept(vararg except : Int) : WithColumnHeadersView {
    return readOnlyViewColumns { index -> index !in except }
}

/**
 * TODO
 */
fun WithColumnHeaders.readOnlyViewColumns(predicate : (Int) -> Boolean) : WithColumnHeadersView {
    return readOnlyViewColumns { index, _ -> predicate(index) }
}

/**
 * TODO
 */
fun WithColumnHeaders.readOnlyViewColumns(predicate : (Int, DataColumnHeader) -> Boolean) : WithColumnHeadersView {
    return readOnlyViewColumns(
        indexIterator(numColumns).filter{ predicate(it, getColumnHeader(it)) }.collect(OrderedHashSet())
    )
}
