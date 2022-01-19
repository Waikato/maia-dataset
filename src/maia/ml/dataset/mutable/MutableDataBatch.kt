package maia.ml.dataset.mutable

import maia.ml.dataset.DataBatch
import maia.ml.dataset.DataRow
import maia.ml.dataset.type.DataRepresentation

/**
 * A data-batch where the data can be modified.
 *
 * @param R     The type of row returned by this structure.
 * @param C     The type of column returned by this structure.
 */
interface MutableDataBatch<out R : DataRow> : DataBatch<R>, WithMutableRows<DataRow, R> {

        fun <T> setColumn(
                representation: DataRepresentation<*, *, in T>,
                column: Collection<T>
        )

        fun clearColumn(
                columnIndex: Int
        )

        fun <T> setValue(
                representation: DataRepresentation<*, *, in T>,
                rowIndex: Int,
                value: T
        )

        fun <T> setValues(
                representation : DataRepresentation<*, *, in T>,
                rowIndex: Int,
                values: Collection<T>
        )

        fun clearValue(
                columnIndex: Int,
                rowIndex: Int
        )

        fun clearValues(
                columnIndex : Int,
                rowIndex : Int,
                count: Int
        )
}
