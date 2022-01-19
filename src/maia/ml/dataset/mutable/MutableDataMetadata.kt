package maia.ml.dataset.mutable

import maia.ml.dataset.DataMetadata

/**
 * Interface for data-set metadata that can be modified.
 */
interface MutableDataMetadata : DataMetadata {

    override var name: String

}
