package māia.ml.dataset.util

import māia.ml.dataset.WithColumnHeaders
import māia.ml.dataset.error.DifferentColumnStructure
import māia.util.asIterable
import māia.util.datastructure.MutableOrderedSet
import māia.util.datastructure.buildOrderedSet
import māia.util.enumerate

/**
 * Gets the indexed positions of these headers in another set of headers.
 *
 * @receiver        The headers to locate.
 * @param other     The headers to locate these headers within.
 * @return          The ordered set of column indices.
 */
fun WithColumnHeaders.getHeaderSubsetIndices(other : WithColumnHeaders) : MutableOrderedSet<Int> {
    return buildOrderedSet {
        for (header in iterateColumnHeaders()) {
            add(
                    other
                            .iterateColumnHeaders()
                            .enumerate()
                            .asIterable()
                            .firstOrNull { (_, trainHeader) -> trainHeader === header }
                            ?.first ?: throw DifferentColumnStructure()
            )
        }
    }
}
