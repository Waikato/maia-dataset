package maia.ml.dataset

import maia.ml.dataset.headers.header.DataColumnHeader

/**
 * Interface for structures representing a single column of
 * a data-set.
 *
 * @param T     The internal type of data in the column.
 */
interface DataColumn<out T> :
        WithIndexableRows<T> {

    /** The header describing the column. */
    val header : DataColumnHeader

}
