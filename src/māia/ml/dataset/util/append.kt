package māia.ml.dataset.util

/*
 * Provides 'append' methods for data-structures which can mutate
 * their row/column structure.
 */

import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.error.DifferentColumnStructure
import māia.ml.dataset.error.DifferentRowStructure
import māia.ml.dataset.mutable.WithMutableColumnStructure
import māia.ml.dataset.mutable.WithMutableRowStructure

/**
 * Appends a row to the end of a data-structure.
 *
 * @receiver                            The data-structure to append the row to.
 * @param value                         The value to set the row to.
 * @throws DifferentColumnStructure     If the row doesn't have the same column-structure
 *                                      as the receiver.
 * @param Rin                           The type of row-value receivable by the data-structure.
 */
fun <Rin> WithMutableRowStructure<Rin, *>.appendRow(value : Rin) {
    insertRow(numRows, value)
}

/**
 * Appends a column to the end of a data-structure.
 *
 * @receiver                            The data-structure to append the column to.
 * @param header                        The header to give the column.
 * @param column                        The value to set the column to.
 * @throws DifferentRowStructure        If the column doesn't have the same row-structure
 *                                      as the receiver.
 * @param Cin                           The type of data kept in the column.
 */
fun <Cin> WithMutableColumnStructure<Cin, *>.appendColumn(
        header : DataColumnHeader,
        column : Cin
) {
    insertColumn(numColumns, header, column)
}
