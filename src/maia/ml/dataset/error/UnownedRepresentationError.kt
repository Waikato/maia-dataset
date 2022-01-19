package maia.ml.dataset.error

import maia.ml.dataset.headers.DataColumnHeaders
import maia.ml.dataset.type.DataRepresentation

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class UnownedRepresentationError(
    representation : DataRepresentation<*, *, *>,
    headers: DataColumnHeaders
): Exception(
    "$representation is not owned by headers@${System.identityHashCode(headers)}"
)
