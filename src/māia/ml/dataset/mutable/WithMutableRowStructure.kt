package māia.ml.dataset.mutable

import māia.ml.dataset.error.DifferentColumnStructure

/**
 * Interface for data-sets which can not only modify the values
 * in rows, but also add/remove rows as well.
 *
 * @param Rin   The type of row required to set a row's value.
 * @param Rout  The type of row returned by the structure.
 */
interface WithMutableRowStructure<in Rin, out Rout> : WithMutableRows<Rin, Rout> {

    /**
     * Inserts a row at the given index.
     *
     * @param rowIndex                      The index at which to insert the row.
     * @param value                         The value to set the row to.
     * @throws IndexOutOfBoundsException    If the index of the row is out of range.
     * @throws DifferentColumnStructure     If the row doesn't have the same structure
     *                                      as the data-set.
     */
    fun insertRow(rowIndex : Int, value : Rin)

    fun insertRows(rowIndex: Int, values: Collection<Rin>)

    /**
     * Deletes the row at the given index.
     *
     * @param rowIndex                      The index at which to delete the row.
     * @throws IndexOutOfBoundsException    If the index of the row is out of range.
     */
    fun deleteRow(rowIndex : Int)

    fun deleteRows(rowIndex: Int, count: Int)

}
