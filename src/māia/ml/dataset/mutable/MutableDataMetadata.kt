package māia.ml.dataset.mutable

import māia.ml.dataset.DataMetadata

/**
 * Interface for data-set metadata that can be modified.
 */
interface MutableDataMetadata : DataMetadata {

    override var name: String

}
