package maia.ml.dataset

import maia.ml.dataset.headers.DataColumnHeaders

/**
 * Interface for structures which have a number of typed columns,
 * but access is limited to the headers of those columns.
 */
interface WithColumns {

    val headers: DataColumnHeaders

    val numColumns: Int
        get() = headers.size

}
