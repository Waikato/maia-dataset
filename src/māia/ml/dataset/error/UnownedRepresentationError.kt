package māia.ml.dataset.error

import māia.ml.dataset.headers.DataColumnHeaders
import māia.ml.dataset.type.DataRepresentation

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
