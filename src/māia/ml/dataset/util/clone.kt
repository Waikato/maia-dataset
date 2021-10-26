package māia.ml.dataset.util

/*
 * Provides 'clone' methods for copying data in bulk from one data-structure
 * to another.
 */

import māia.ml.dataset.DataColumn
import māia.ml.dataset.DataRow
import māia.ml.dataset.error.*
import māia.ml.dataset.mutable.MutableDataColumn
import māia.ml.dataset.mutable.MutableDataRow
import māia.ml.dataset.type.DataRepresentation
import māia.util.enumerate

/**
 * Clones the values from one data-column to another.
 *
 * @receiver                        The mutable data-column to receive the values.
 * @param other                     The data-column to clone values from.
 * @throws DifferentColumnHeader    If the source column has a different header to the receiving column.
 * @throws DifferentRowStructure    If the number of rows in the source column is different to the receiving column.
 */
fun <T> MutableDataColumn<T>.clone(other : DataColumn<T>) {
    // Make sure the source column matches this column's structure
    header mustBeSameTypeAs other.header

    // Copy the value from each row of the source to this data-column
    for ((rowIndex, value) in other.rowIterator().enumerate()) {
        setRow(rowIndex, value)
    }
}

/**
 * Clones the values from one data-row to another.
 *
 * @receiver                            The mutable data-row to receive the values.
 * @param other                         The data-row to clone values from.
 * @throws DifferentColumnStructure     If the columns of the source don't match the receiver.
 */
fun MutableDataRow.clone(other : DataRow) {
    // Make sure the source row matches this row's structure
    this mustHaveEquivalentColumnStructureTo other

    // Copy the value from each row of the source to this data-row
    forEachColumn { index, header ->
        val repr = header.type.canonicalRepresentation
        val value = other.getValue(repr)
        setValue(repr as DataRepresentation<*, *, in Any?>, value)
    }
}

// TODO: MutableDataBatch
