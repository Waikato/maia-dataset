package māia.ml.dataset.mutable

import māia.ml.dataset.WithColumns
import māia.ml.dataset.error.DifferentColumnStructure

/**
 * Interface for columned structures that can have the data
 * in their columns modified.
 *
 * @param Cin   The type of column received by this structure.
 * @param Cout  The type of column returned by this structure.
 */
interface WithMutableColumns<in Cin, out Cout> : WithColumns<Cout> {

    /**
     * Sets the value of a column.
     *
     * @param columnIndex   The index of the column to set.
     * @param column        The value to set the column to.
     * @throws IndexOutOfBoundsException    If the column index is not valid
     *                                      for the structure.
     * @throws DifferentColumnStructure     If the column structure of the
     *                                      provided value doesn't match this
     *                                      structure.
     */
    fun setColumn(columnIndex : Int, column : Cin)

}
