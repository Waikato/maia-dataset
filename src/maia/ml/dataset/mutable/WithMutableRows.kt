package maia.ml.dataset.mutable

import maia.ml.dataset.WithIndexableRows
import maia.ml.dataset.error.DifferentColumnStructure

/**
 * Interface for data-sets which can modify the values of
 * a row of data.
 *
 * @param Rin   The type of row required to set a row's value.
 * @param Rout  The type of row returned by the structure.
 */
interface WithMutableRows<in Rin, out Rout> : WithIndexableRows<Rout> {

    /**
     * Sets the value of a row in this data-set.
     *
     * @param rowIndex                      The index of the row to modify.
     * @param value                         The value to set the row to.
     * @throws IndexOutOfBoundsException    If the index of the row is out of range.
     * @throws DifferentColumnStructure     If the row doesn't have the same structure
     *                                      as the data-set.
     */
    fun setRow(rowIndex : Int, value : Rin)

    fun setRows(rowIndex : Int, values: Collection<Rin>)

    fun clearRow(rowIndex : Int)

    fun clearRows(rowIndex: Int, count: Int)

}
