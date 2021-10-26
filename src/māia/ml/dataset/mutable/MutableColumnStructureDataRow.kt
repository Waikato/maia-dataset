package māia.ml.dataset.mutable

import māia.ml.dataset.type.DataRepresentation
import māia.ml.dataset.type.DataType

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
interface MutableColumnStructureDataRow: MutableDataRow {

    /**
     * Deletes the column at the specified index from the data-row.
     *
     * @param columnIndex
     *          The index of the column to delete.
     * @param columnName
     *          Optionally specifies the name of the column as well.
     *          If provided, deletion will fail if the name doesn't
     *          match the index.
     * @return
     *          The name of the deleted column.
     */
    fun deleteColumn(columnIndex: Int, columnName : String? = null): String

    /**
     * Deletes the column with the specified name from the data-row.
     *
     * @param columnName
     *          The name of the column to delete.
     * @param columnIndex
     *          Optionally specifies the index of the column as well.
     *          If provided, deletion will fail if the index doesn't
     *          match the name.
     * @return
     *          The index of the deleted column.
     */
    fun deleteColumn(columnName: String, columnIndex : Int? = null): Int

    /**
     * Inserts a column with data into the data-row.
     *
     * TODO: params
     */
    fun <T> insertColumn(
        index : Int,
        name : String,
        representation : DataRepresentation<*, *, in T>,
        isTarget : Boolean,
        data : T,
        supportsMissingValues: Boolean = representation.dataType.supportsMissingValues
    )

    /**
     * Inserts a column without data into the data-row.
     *
     * @throws DoesntSupportMissingValues
     *          If the underlying data-storage doesn't support missing
     *          values.
     * @throws MissingValue
     *          If the
     */
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
        data: T,
        supportsMissingValues: Boolean = representation.dataType.supportsMissingValues
    )

    fun changeColumn(
        index: Int,
        name: String,
        type : DataType<*, *>,
        isTarget: Boolean
    )

    fun clearColumns()
}
