package māia.ml.dataset

/**
 * Interface for structures representing a single column of
 * a data-set.
 *
 * @param T     The internal type of data in the column.
 */
interface DataColumn :
        WithIndexableRows<Any?> {

    /** The header describing the column. */
    val header : DataColumnHeader

}
