package maia.ml.dataset.util

import maia.ml.dataset.headers.DataColumnHeaders
import maia.ml.dataset.headers.MutableDataColumnHeaders
import maia.util.datastructure.OrderedSet
import maia.util.enumerate
import maia.util.indexIterator
import maia.util.map

/**
 * TODO
 */
fun DataColumnHeaders.copy(
    columns : OrderedSet<Int>? = null
) : MutableDataColumnHeaders {
    // Create an empty set of headers to copy into
    val result = MutableDataColumnHeaders(columns?.size ?: size)

    // Get an iterator over the columns to copy
    val columnIterator = columns?.iterator() ?: indexIterator(size)

    // Add a copy of each header in this set to the new set
    columnIterator                      // For each column
        .map { this[it] }               // Get the header in this set
        .enumerate()                    // Get its index in the new set
        .forEach { (index, header) ->   // Create a copy of our header in the new set at the new index
            val name = header.name
            result.headersInternal.add(result.createHeader(index, name, header.type, header.isTarget))
            result.names.add(name)
            result.nameToIndexMap[name] = index
        }

    return result
}
