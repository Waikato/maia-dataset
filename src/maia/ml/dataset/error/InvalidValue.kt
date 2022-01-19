package maia.ml.dataset.error

import maia.ml.dataset.type.DataRepresentation

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class InvalidValue(
    representation: DataRepresentation<*, *, *>,
    value: Any?
): Exception(
    "Value $value is not valid for $representation of ${representation.dataType}"
)

