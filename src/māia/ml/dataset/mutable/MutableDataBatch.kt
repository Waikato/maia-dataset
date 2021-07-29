package māia.ml.dataset.mutable

import māia.ml.dataset.DataBatch
import māia.ml.dataset.DataColumn
import māia.ml.dataset.DataRow

/**
 * A data-batch where the data can be modified.
 *
 * @param R     The type of row returned by this structure.
 * @param C     The type of column returned by this structure.
 */
interface MutableDataBatch<out R : DataRow, out C : DataColumn> :
        DataBatch<R, C>,
        WithMutableRows<DataRow, R>,
        WithMutableColumns<DataColumn, C> {

    /**
     * Sets the value of a particular cell in the data-set.
     *
     * @param rowIndex                      The index of the row to set the
     *                                      value in.
     * @param columnIndex                   The index of the column to set the
     *                                      value in.
     * @param value                         The value to set the cell to.
     * @throws IndexOutOfBoundsException    If the row/column index is outside
     *                                      if the data-set.
     * @throws ClassCastException           If the value is of the wrong type
     *                                      for the column.
     */
    fun setValue(rowIndex : Int, columnIndex : Int, value : Any?)

}
