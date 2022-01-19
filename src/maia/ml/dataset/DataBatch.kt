package maia.ml.dataset

import maia.ml.dataset.type.DataRepresentation

/**
 * Represents a data-set in memory. Unlike a data-stream, the data
 * of a batch can be accessed in arbitrary order.
 *
 * @param R     The type of row returned by this structure.
 * @param C     The type of column returned by this structure.
 */
interface DataBatch<out R : DataRow> : DataStream<R>, WithIndexableRows<R> {
        /**
         * Gets a column of the structure.
         *
         * @param columnIndex                   The index of the column to get.
         * @return                              The column.
         * @throws IndexOutOfBoundsException    If the column index is not within range
         *                                      for the structure.
         */
        fun <T> getColumn(representation: DataRepresentation<*, *, T>) : DataColumn<T>

        /**
         * Gets a value from the data-row
         *
         * @param columnIndex                   The index of the column to get.
         * @return                              The column.
         * @throws IndexOutOfBoundsException    If the column index is not within range
         *                                      for the structure.
         */
        fun <T> getValue(representation: DataRepresentation<*, *, out T>, rowIndex: Int) : T
}
