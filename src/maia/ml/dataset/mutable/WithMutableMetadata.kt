package maia.ml.dataset.mutable

import maia.ml.dataset.WithMetadata

/**
 * Interface for meta-data of data-sets which can be modified.
 */
interface WithMutableMetadata : WithMetadata {

    override val metadata: MutableDataMetadata

    override var name: String
        get() = super.name
        set(value) { metadata.name = value }

}
