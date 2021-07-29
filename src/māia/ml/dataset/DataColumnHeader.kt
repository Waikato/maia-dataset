package māia.ml.dataset

import māia.ml.dataset.type.DataType

/**
 * Interface for headers to data-columns, which describe the type
 * of data in the column, whether the data is purposed for being
 * the target of training, and a name for the column.
 */
interface DataColumnHeader {

    /** The name of the column. */
    val name : String

    /** The type of data in the column. */
    val type : DataType<*, *>

    /** Whether this column is target data (false is feature data). */
    val isTarget : Boolean

}
