package maia.ml.dataset.mutable

import maia.ml.dataset.DataColumn

/**
 * Interface for structures representing a single column of
 * a data-set, where the data in the column can be mutated.
 */
interface MutableDataColumn<T> :
        DataColumn<T>,
        WithMutableRows<T, T>
