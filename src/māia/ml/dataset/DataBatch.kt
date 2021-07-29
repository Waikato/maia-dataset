package māia.ml.dataset

/**
 * Represents a data-set in memory. Unlike a data-stream, the data
 * of a batch can be accessed in arbitrary order.
 *
 * @param R     The type of row returned by this structure.
 * @param C     The type of column returned by this structure.
 */
interface DataBatch<out R : DataRow, out C : DataColumn> :
        DataStream<R>,
        WithColumns<C>,
        WithIndexableRows<R> {

    /**
     * Gets the value of the indexed cell of the data-set.
     *
     * @param rowIndex                      The index of the row to get the data from.
     * @param columnIndex                   The index of the column to get the data from.
     * @return                              The value in the specified cell.
     * @throws IndexOutOfBoundsException    If the data-set has no cell at the specified
     *                                      row/column.
     */
    fun getValue(rowIndex : Int, columnIndex : Int) : Any?

}
