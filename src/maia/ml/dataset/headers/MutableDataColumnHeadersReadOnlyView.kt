package maia.ml.dataset.headers

/**
 * A read-only view of a set of mutable data-column headers.
 *
 * @param source
 *          The source set of mutable headers.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class MutableDataColumnHeadersReadOnlyView(
    internal val source: MutableDataColumnHeadersBase
): DataColumnHeaders by source
