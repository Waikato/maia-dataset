package maia.ml.dataset.error

import maia.ml.dataset.type.DataRepresentation

/**
 * Exception for when a value is missing.
 */
class MissingValue: Exception {

    constructor(): super()

    constructor(
        representation: DataRepresentation<*, *, *>
    ): super(message(representation))

    constructor(
        representation: DataRepresentation<*, *, *>,
        cause: Throwable
    ): super(message(representation), cause)

    companion object {
        private fun message(representation: DataRepresentation<*, *, *>): String
        = "Value in column ${representation.columnIndex} \"${representation.name}\" has no value"
    }
}
