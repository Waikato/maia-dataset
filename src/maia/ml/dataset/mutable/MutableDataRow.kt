package maia.ml.dataset.mutable

import maia.ml.dataset.DataRow
import maia.ml.dataset.type.DataRepresentation

/**
 * Interface for a single row in a data-set. The values in the row are mutable.
 */
interface MutableDataRow : DataRow {
        fun <T> setValue(representation: DataRepresentation<*, *, in T>, value: T)
        fun clearValue(columnIndex: Int)
}
