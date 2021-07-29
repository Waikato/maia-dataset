package māia.ml.dataset

import māia.util.ElementIterator

/**
 * Interface for structures which have a number of typed columns,
 * but access is limited to the headers of those columns.
 */
interface WithColumnHeaders {

    /** The number of columns in the structure. */
    val numColumns : Int

    /**
     * Gets the header for the indexed column.
     *
     * @param columnIndex   The index of the column.
     * @return              The header of the column.
     */
    fun getColumnHeader(columnIndex : Int) : DataColumnHeader

    /**
     * Iterates through the headers of the columns of the structure.
     *
     * @return  An iterator over the column headers of the structure.
     */
    fun iterateColumnHeaders() : Iterator<DataColumnHeader> {
        // Default implementation is to call getColumnHeader for each column index
        return ElementIterator(this::getColumnHeader, numColumns)
    }
}
