package māia.ml.dataset

import māia.util.ElementIterator

/**
 * Interface for structures which have columns, and those
 * columns can be retrieved.
 *
 * @param C     The type of column returned by this structure.
 */
interface WithColumns<out C> :
        WithColumnHeaders {

    /**
     * Gets a column of the structure.
     *
     * @param columnIndex                   The index of the column to get.
     * @return                              The column.
     * @throws IndexOutOfBoundsException    If the column index is not within range
     *                                      for the structure.
     */
    fun getColumn(columnIndex : Int) : C

    /**
     * Iterates over the columns of this structure.
     *
     * @return  An iterator over the columns of the structure.
     */
    fun iterateColumns() : Iterator<C> {
        // Default implementation is to call getColumn for every column index
        return ElementIterator(this::getColumn, numColumns)
    }
}
