package māia.ml.dataset.util

import māia.ml.dataset.DataRow

/**
 * Gets the value of a column in the data-row, converted to its
 * external type.
 */
fun DataRow.getValueExternal(columnIndex: Int): Any? =
    getColumnHeader(columnIndex)
        .type
        .convertToExternalUnchecked(getColumn(columnIndex))
