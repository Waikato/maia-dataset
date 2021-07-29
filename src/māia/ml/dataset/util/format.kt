package māia.ml.dataset.util

import māia.ml.dataset.DataRow
import māia.util.enumerate
import māia.util.joinToString

/**
 * TODO
 */
fun DataRow.formatString() : String {
    return iterateColumnHeaders()
            .enumerate()
            .joinToString(prefix = "[", postfix = "]") { (index, header) ->
                "${header.name}: ${header.type.convertToExternalUnchecked(getColumn(index))}"
            }
}

/**
 * TODO
 */
fun DataRow.formatStringSimple() : String {
    return buildString {
        forEachColumn { index, header, value ->
            append(header.type.convertToExternalUnchecked(value))
            append(",")
        }
    }
}
