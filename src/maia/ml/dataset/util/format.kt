package maia.ml.dataset.util

import maia.ml.dataset.DataRow
import maia.ml.dataset.error.MissingValue
import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.type.DataType

/**
 * TODO
 */
fun DataRow.formatString() : String {
    return headers
        .joinToString(prefix = "[", postfix = "]") { header ->
            "${header.name}: ${formatValue(header)}"
        }
}

/**
 * TODO
 */
fun DataRow.formatStringSimple() : String {
    return buildString {
        forEachColumn { _, header ->
            append(formatValue(header))
            append(",")
        }
    }
}

/**
 * Formats a value from a [DataRow] as a string.
 *
 * @receiver The [DataRow] to get the value from.
 * @param header
 *          A header from the [DataRow] selecting which value to format.
 * @return A [String] representation of the value.
 */
inline fun DataRow.formatValue(
    header: DataColumnHeader
): String = formatValue(header.type)

/**
 * Formats a value from a [DataRow] as a string.
 *
 * @receiver The [DataRow] to get the value from.
 * @param type
 *          A data-type from the [DataRow] selecting which value to format.
 * @return A [String] representation of the value.
 */
inline fun DataRow.formatValue(
    type: DataType<*, *>
): String {
    return try {
        getValue(type.canonicalRepresentation).toString()
    } catch (e: MissingValue) {
        "<missing>"
    }
}
