package māia.ml.dataset.headers

import māia.ml.dataset.error.UnownedRepresentationError
import māia.ml.dataset.headers.header.DataColumnHeader
import māia.ml.dataset.type.DataRepresentation
import māia.ml.dataset.util.getEquivalentRepresentation
import māia.util.datastructure.buildOrderedSet
import māia.util.indexInRange

/**
 * Builds a view of the receiving headers by picking the columns to view
 * with a predicate function.
 *
 * @receiver
 *          The headers to create a view of.
 * @param predicate
 *          Function which takes each index and header and header and
 *          decides whether to include it in the view.
 * @return
 *          The view of the headers.
 */
inline fun DataColumnHeaders.viewColumns(
    crossinline predicate: (Int, DataColumnHeader) -> Boolean
): DataColumnHeadersView {
    return DataColumnHeadersView(
        this,
        buildOrderedSet {
            this@viewColumns.forEachIndexed { index, header ->
                if (predicate(index, header))
                    add(index)
            }
        }
    )
}

/**
 * Executes the given [block] of code with the equivalent [representation]
 * as owned by this set of headers.
 *
 * @param representation
 *          The [DataRepresentation] that we should find.
 * @param block
 *          The code to execute under our own equivalent representation.
 *
 * @return The result of [block].
 *
 * @throws UnownedRepresentationError
 *          If the representation doesn't have an equivalent in these headers.
 */
inline fun <H: DataColumnHeaders, T, R> H.ensureOwnership(
    representation : DataRepresentation<*, *, T>,
    block: DataRepresentation<*, *, T>.() -> R
): R {
    return ownedEquivalent(representation)?.block()
        ?: throw UnownedRepresentationError(representation, this)
}


/**
 * Gets the owned column header that is equivalent to the given one.
 *
 * @param other
 *          Any column header.
 * @return
 *          Our equivalent header, or null if we don't have one.
 */
inline fun DataColumnHeaders.getEquivalentHeader(
    other: DataColumnHeader
): DataColumnHeader? {
    // Get the index of the other header
    val index = other.index

    // Make sure it's an accessible index for us
    if (!indexInRange(index, size)) return null

    // Get our header at that index
    val header = this[index]

    // Make sure they're equivalent
    return if (header isEquivalentTo other)
        header
    else
        null
}

/**
 * TODO
 */
inline fun <T> DataColumnHeaders.getEquivalentRepresentation(
    other: DataRepresentation<*, *, T>
): DataRepresentation<*, *, T>? {
    val header = other.headerIdentityToken?.let { getEquivalentHeader(it) }
    return header?.type?.getEquivalentRepresentation(other)
}
