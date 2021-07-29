package māia.ml.dataset.mutable

import māia.ml.dataset.DataRow

/**
 * Interface for a single row in a data-set. The values in the row are mutable.
 */
interface MutableDataRow :
        DataRow,
        WithMutableColumns<Any?, Any?>
