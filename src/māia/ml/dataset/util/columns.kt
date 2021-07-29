package māia.ml.dataset.util

/*
 * Provides overloads of methods for data-structures with mutable
 * column structure, where the column-type is a type of data-column,
 * so the column header can be retrieved from the column value itself.
 */

import māia.ml.dataset.DataColumn
import māia.ml.dataset.error.DifferentRowStructure
import māia.ml.dataset.mutable.WithMutableColumnStructure

/**
 * Inserts a column at the specified index.
 *
 * @receiver                            The data-structure to insert the column into.
 * @param columnIndex                   The index at which to insert the column.
 * @param column                        The value of the column to insert.
 * @throws IndexOutOfBoundsException    If the index is not valid for the data-structure.
 * @throws DifferentRowStructure        If the row-structure of the column doesn't
 *                                      match this data-structure.
 */
fun <Cin : DataColumn> WithMutableColumnStructure<Cin, *>.insertColumn(
        columnIndex : Int,
        column : Cin
) {
    insertColumn(columnIndex, column.header, column)
}

/**
 * Modifies the column at the specified index.
 *
 * @receiver                            The data-structure to modify.
 * @param columnIndex                   The index of the column to modify.
 * @param column                        The value to give the column.
 * @throws IndexOutOfBoundsException    If the index is not valid for the data-structure.
 * @throws DifferentRowStructure        If the row-structure of the column doesn't
 *                                      match this data-structure.
 */
fun <Cin : DataColumn> WithMutableColumnStructure<Cin, *>.changeColumn(
        columnIndex : Int,
        column : Cin
) {
    changeColumn(columnIndex, column.header, column)
}

/**
 * Appends a column to the end of a data-structure.
 *
 * @receiver                            The data-structure to append the column to.
 * @param column                        The value to set the column to.
 * @throws DifferentRowStructure        If the column doesn't have the same row-structure
 *                                      as the receiver.
 * @param Cin                           The type of data kept in the column.
 */
fun <Cin : DataColumn> WithMutableColumnStructure<Cin, *>.appendColumn(
        column : Cin
) {
    insertColumn(numColumns, column)
}
