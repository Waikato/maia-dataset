package māia.ml.dataset

import māia.util.indexIterator
import māia.util.map

/**
 * Interface for structures which can return their constituent
 * rows in an arbitrary order.
 *
 * @param R     The type of row returned by the structure.
 */
interface WithIndexableRows<out R> :
        WithRows<R> {

    /** The number of rows in the structure. */
    val numRows : Int

    /**
     * Gets an arbitrary row by index.
     *
     * @param rowIndex                      The position of the row in the structure.
     * @return                              The row.
     * @throws IndexOutOfBoundsException    If the row index is invalid for this structure.
     */
    fun getRow(rowIndex: Int) : R

    override fun rowIterator() : Iterator<R> {
        // Default implementation of row-iteration which calls getRow for each row index
        return indexIterator(numRows).map { getRow(it) }
    }

}
