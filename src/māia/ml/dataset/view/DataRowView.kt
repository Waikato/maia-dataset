package māia.ml.dataset.view

import māia.util.datastructure.OrderedHashSet
import māia.util.datastructure.OrderedSet
import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.DataRow
import māia.ml.dataset.util.translateColumn

/**
 * A read-only view of a sub-set of a data-row.
 *
 * @param source    The data-row to use as the source of values.
 * @param columns   The columns of the row to view, or null for all columns.
 */
open class DataRowView(
        protected val source : DataRow,
        protected val columns : OrderedSet<Int>? = null
) : DataRow {

    constructor(source : DataRow, columns : Iterator<Int>) : this(source, OrderedHashSet<Int>().also { set ->
        columns.forEach { set.add(it) }
    })

    constructor(source : DataRow, vararg columns : Int) : this(source, columns.iterator())

    override val numColumns: Int
        get() = columns?.size ?: source.numColumns

    override fun getColumn(columnIndex : Int) : Any? {
        return source.getColumn(translateColumn(columns, columnIndex))
    }

    override fun getColumnHeader(columnIndex: Int): DataColumnHeader {
        return source.getColumnHeader(translateColumn(columns, columnIndex))
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
