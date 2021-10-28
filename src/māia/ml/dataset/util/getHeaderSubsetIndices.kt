package māia.ml.dataset.util

import māia.ml.dataset.WithColumns
import māia.ml.dataset.error.DifferentColumnStructure
import māia.util.datastructure.MutableOrderedSet
import māia.util.datastructure.buildOrderedSet

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
                otherHeaders.indexOf(header).also { if (it < 0) throw DifferentColumnStructure() }
            )
        }
    }
}
