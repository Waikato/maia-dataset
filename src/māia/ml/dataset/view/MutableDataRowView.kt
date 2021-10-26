package māia.ml.dataset.view

import māia.util.datastructure.OrderedHashSet
import māia.util.datastructure.OrderedSet
import māia.ml.dataset.mutable.MutableDataRow
import māia.ml.dataset.type.DataRepresentation
import māia.ml.dataset.util.translateColumn

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
