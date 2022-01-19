package maia.ml.dataset.util

import maia.ml.dataset.DataRow
import maia.ml.dataset.error.UnownedRepresentationError
import maia.ml.dataset.headers.MutableDataColumnHeaders
import maia.ml.dataset.type.DataRepresentation

/**
 * Utility row which contains no values.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
object EmptyRow: DataRow {
    override val headers = MutableDataColumnHeaders(0).readOnlyView
    override fun <T> getValue(representation : DataRepresentation<*, *, out T>) : T {
        throw UnownedRepresentationError(representation, headers)
    }
}
