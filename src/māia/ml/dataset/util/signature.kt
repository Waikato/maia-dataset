package māia.ml.dataset.util

import māia.ml.dataset.WithColumns

/**
 * Extension method which returns the column-header signature of
 * the receiving structure.
 *
 * @receiver    The structure of which to cache the headers.
 * @return      The column-header signature.
 */
fun WithColumns.signature() : WithColumns {
    // Take a mutable copy of the headers, then throw away mutable access
    return headers.copy().readOnlyView
}
