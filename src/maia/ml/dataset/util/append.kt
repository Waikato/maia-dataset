package maia.ml.dataset.util

/*
 * Provides 'append' methods for data-structures which can mutate
 * their row/column structure.
 */

import maia.ml.dataset.error.DifferentColumnStructure
import maia.ml.dataset.mutable.MutableColumnStructureDataBatch
import maia.ml.dataset.mutable.MutableColumnStructureDataRow
import maia.ml.dataset.mutable.WithMutableRowStructure
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.type.DataType

/**
 * Appends a row to the end of a data-structure.
 *
 * @receiver                            The data-structure to append the row to.
 * @param value                         The value to set the row to.
 * @throws DifferentColumnStructure     If the row doesn't have the same column-structure
 *                                      as the receiver.
 * @param Rin                           The type of row-value receivable by the data-structure.
 */
inline fun <Rin> WithMutableRowStructure<Rin, *>.appendRow(value: Rin) = insertRow(numRows, value)

inline fun <Rin> WithMutableRowStructure<Rin, *>.appendRows(values: Collection<Rin>) = insertRows(numRows, values)

inline fun <T> MutableColumnStructureDataRow.appendColumn(
    name: String,
    representation : DataRepresentation<*, *, in T>,
    isTarget: Boolean,
    data: T,
    supportsMissingValues: Boolean = representation.dataType.supportsMissingValues
) = insertColumn(
    headers.size,
    name,
    representation,
    isTarget,
    data,
    supportsMissingValues
)

inline fun MutableColumnStructureDataRow.appendColumn(
    name: String,
    type : DataType<*, *>,
    isTarget: Boolean
) = insertColumn(headers.size, name, type, isTarget)


inline fun <T> MutableColumnStructureDataBatch<*>.appendColumn(
    name: String,
    representation : DataRepresentation<*, *, in T>,
    isTarget: Boolean,
    supportsMissingValues: Boolean = representation.dataType.supportsMissingValues,
    noinline data: (Int) -> T
) = insertColumn(headers.size, name, representation, isTarget, supportsMissingValues, data)

inline fun MutableColumnStructureDataBatch<*>.appendColumn(
    name: String,
    type : DataType<*, *>,
    isTarget: Boolean
) = insertColumn(headers.size, name, type, isTarget)
