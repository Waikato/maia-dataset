package maia.ml.dataset.mutable

import maia.ml.dataset.DataRow
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.type.DataType

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
interface MutableColumnStructureDataBatch<out R : DataRow> : MutableDataBatch<R> {

    fun deleteColumn(columnIndex: Int, columnName : String? = null): String

    fun deleteColumn(columnName: String, columnIndex : Int? = null): Int

    fun <T> insertColumn(
        index: Int,
        name: String,
        representation : DataRepresentation<*, *, in T>,
        isTarget: Boolean,
        supportsMissingValues: Boolean,
        data: (Int) -> T
    )

    fun insertColumn(
        index: Int,
        name: String,
        type : DataType<*, *>,
        isTarget: Boolean
    )

    fun <T> changeColumn(
        index: Int,
        name: String,
        representation : DataRepresentation<*, *, in T>,
        isTarget: Boolean,
        supportsMissingValues: Boolean,
        data: (Int) -> T
    )

    fun changeColumn(
        index: Int,
        name: String,
        type : DataType<*, *>,
        isTarget: Boolean
    )

    fun clearColumns()
}
