package maia.ml.dataset.util

import maia.ml.dataset.WithColumns
import maia.ml.dataset.error.DifferentColumnStructure
import maia.util.datastructure.MutableOrderedSet
import maia.util.datastructure.buildOrderedSet

/**
 * Gets the indexed positions of these headers in another set of headers.
 *
 * @receiver        The headers to locate.
 * @param other     The headers to locate these headers within.
 * @return          The ordered set of column indices.
 */
fun WithColumns.getHeaderSubsetIndices(other : WithColumns) : MutableOrderedSet<Int> {
    val otherHeaders = other.headers
    return buildOrderedSet {
        for (header in headers) {
            add(
                otherHeaders.find {
                    // Can't use isEquivalentTo as indices differ in general
                    it.name == header.name
                            && it isSameTypeAs header
                            && it.isTarget == header.isTarget
                }.let {
                    it?.index ?: throw DifferentColumnStructure()
                }
            )
        }
    }
}
