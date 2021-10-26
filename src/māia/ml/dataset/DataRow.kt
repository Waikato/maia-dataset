package māia.ml.dataset

import māia.ml.dataset.type.DataRepresentation

/**
 * Interface for a single row in a data-set.
 */
interface DataRow : WithColumns {
        /**
         * Gets a value from the data-row
         *
         * @param columnIndex                   The index of the column to get.
         * @return                              The column.
         * @throws IndexOutOfBoundsException    If the column index is not within range
         *                                      for the structure.
         */
        fun <T> getValue(representation: DataRepresentation<*, *, out T>) : T
}
