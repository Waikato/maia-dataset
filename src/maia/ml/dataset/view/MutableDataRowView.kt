package maia.ml.dataset.view

import maia.util.datastructure.OrderedHashSet
import maia.util.datastructure.OrderedSet
import maia.ml.dataset.mutable.MutableDataRow
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.util.translateColumn

/**
 * A mutable view of a sub-set of a data-row.
 *
 * @param source    The data-row to use as the source of values.
 * @param columns   The columns of the row to view, or null for all columns.
 */
class MutableDataRowView(
        override val source : MutableDataRow,
        columns : OrderedSet<Int>? = null
) : DataRowView(source, columns), MutableDataRow {

    constructor(
        source : MutableDataRow,
        columns : Iterator<Int>
    ) : this(
        source,
        OrderedHashSet<Int>().also { set ->
            columns.forEach { set.add(it) }
        } as OrderedSet<Int>?
    )

    constructor(source : MutableDataRow, vararg columns : Int) : this(source, columns.iterator())

    override fun <T> setValue(
        representation : DataRepresentation<*, *, in T>,
        value : T
    ) = source.setValue(representation, value)

    override fun clearValue(columnIndex : Int) = source.clearValue(translateColumn(columns, columnIndex))

}

/**
 * TODO
 */
fun MutableDataRow.mutableView() : MutableDataRowView {
    return MutableDataRowView(this)
}

/**
 * TODO
 */
fun MutableDataRow.mutableViewColumns(columns : OrderedSet<Int>) : MutableDataRowView {
    return MutableDataRowView(this, columns)
}
