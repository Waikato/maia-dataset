package māia.ml.dataset.util

import māia.ml.dataset.DataColumnHeader
import māia.ml.dataset.WithColumnHeaders
import māia.util.collect

/**
 * Class which caches the column headers of a structure.
 *
 * @param list  A mutable list of column headers.
 */
class ColumnHeadersSignature(
        private val list : MutableList<DataColumnHeader>
) : WithColumnHeaders, MutableList<DataColumnHeader> by list {

    /**
     * Constructor which caches the header of the given structure.
     *
     * @param target    The target structure to cache.
     */
    constructor(target : WithColumnHeaders) :
            this(target.iterateColumnHeaders().collect(ArrayList()) as MutableList<DataColumnHeader>)

    override val numColumns : Int
        get() = size

    override fun getColumnHeader(columnIndex : Int) : DataColumnHeader {
        return this[columnIndex]
    }

}

/**
 * Extension method which returns the column-header signature of
 * the receiving structure.
 *
 * @receiver    The structure of which to cache the headers.
 * @return      The column-header signature.
 */
fun WithColumnHeaders.signature() : ColumnHeadersSignature {
    return ColumnHeadersSignature(this)
}
