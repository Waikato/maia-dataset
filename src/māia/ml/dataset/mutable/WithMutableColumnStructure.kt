package māia.ml.dataset.mutable

import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.error.DifferentRowStructure

/**
 * Interface for data-sets where the number/type of columns
 * can be modified.
 *
 * @param Cin   The type of column received by this structure.
 * @param Cout  The type of column returned by this structure.
 */
interface WithMutableColumnStructure<in Cin, out Cout> :
        WithMutableColumns<Cin, Cout> {

    /**
     * Removes the column at the specified index.
     *
     * @param columnIndex                   The index of the column to remove.
     * @throws IndexOutOfBoundsException    If the index is not valid for the data-structure.
     */
    fun deleteColumn(columnIndex : Int)

    /**
     * Inserts a column at the specified index.
     *
     * @param columnIndex                   The index at which to insert the column.
     * @param header                        The header to give the new column.
     * @param column                        The value of the column to insert.
     * @throws IndexOutOfBoundsException    If the index is not valid for the data-structure.
     * @throws DifferentRowStructure        If the row-structure of the column doesn't
     *                                      match this data-structure.
     */
    fun insertColumn(columnIndex : Int, header : DataColumnHeader, column : Cin)

    /**
     * Modifies the column at the specified index.
     *
     * @param columnIndex                   The index of the column to modify.
     * @param header                        The header to give the column.
     * @param column                        The value to give the column.
     * @throws IndexOutOfBoundsException    If the index is not valid for the data-structure.
     * @throws DifferentRowStructure        If the row-structure of the column doesn't
     *                                      match this data-structure.
     */
    fun changeColumn(columnIndex : Int, header : DataColumnHeader, column : Cin)

}
