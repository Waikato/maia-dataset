package māia.ml.dataset.util

import māia.ml.dataset.DataRow
import māia.ml.dataset.error.UnownedRepresentationError
import māia.ml.dataset.headers.MutableDataColumnHeaders
import māia.ml.dataset.type.DataRepresentation

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
