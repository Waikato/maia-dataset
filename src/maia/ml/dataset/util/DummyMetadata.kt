package maia.ml.dataset.util

import maia.ml.dataset.DataMetadata

/**
 * A dummy meta-data object to use when the meta-data
 * doesn't matter.
 */
object DummyMetadata : DataMetadata {
    override val name : String = ""
}
