package māia.ml.dataset.util

/*
 * Provides methods for checking is data-structures are equivalent to one-another.
 */

import māia.ml.dataset.*
import māia.ml.dataset.error.*
import māia.ml.dataset.headers.DataColumnHeaders
import māia.ml.dataset.headers.header.DataColumnHeader
import māia.ml.dataset.type.DataRepresentation
import māia.ml.dataset.type.DataType
import māia.util.indexInRange

// region Type
/**
 * Whether this column has the same type of data as another
 * column.
 *
 * @param other     The column to compare type with.
 * @return          True if the columns both have the same type,
 *                  false otherwise.
 */
inline infix fun DataColumnHeader.isSameTypeAs(other : DataColumnHeader) = type == other.type

/**
 * Whether this column does not have the same type of data as another
 * column.
 *
 * @param other     The column to compare type with.
 * @return          False if the columns both have the same type,
 *                  true otherwise.
 */
inline infix fun DataColumnHeader.isNotSameTypeAs(other : DataColumnHeader) = !(this isSameTypeAs other)

/**
 * Ensures that this column has the same type of data as another
 * column.
 *
 * @param other     The column to compare type with.
 * @throws DifferentDataType    If the column types aren't the same.
 */
inline infix fun DataColumnHeader.mustBeSameTypeAs(other : DataColumnHeader) {
    if (this isNotSameTypeAs other)
        throw DifferentDataType()
}

// endregion

// region Header

/**
 * Whether this column header describes a different column to
 * another.
 *
 * @param other     The other column header.
 * @return          False if the column headers describe the same column,
 *                  true otherwise.
 */
inline infix fun DataColumnHeader.isNotEquivalentTo(other : DataColumnHeader) = !(this isEquivalentTo other)

/**
 * Ensures that this column header describes the same column as
 * another.
 *
 * @param other                     The other column header.
 * @throws DifferentColumnHeader    If the headers are not equivalent.
 */
inline infix fun DataColumnHeader.mustBeEquivalentTo(other : DataColumnHeader) {
    if (this isNotEquivalentTo other)
        throw DifferentColumnHeader()
}

// endregion

// region Structure

/**
 * Checks if the structure of this data-dataset is the same as that of
 * another.
 *
 * @param other     The data-set to compare to.
 * @return          True if the two data-sets have the same structure,
 *                  false if not.
 */
inline infix fun DataBatch<*>.hasEquivalentStructureTo(other : DataBatch<*>) : Boolean {
    return this hasEquivalentColumnStructureTo other && this hasEquivalentRowStructureTo other
}

/**
 * Checks if the structure of this data-dataset is different to that of
 * another.
 *
 * @param other     The data-dataset to compare to.
 * @return          False if the two data-sets have the same structure,
 *                  true if not.
 */
inline infix fun DataBatch<*>.doesNotHaveEquivalentStructureTo(other : DataBatch<*>) = !(this hasEquivalentStructureTo other)

/**
 * Ensures that the structure of this data-dataset is the same as that
 * of another.
 *
 * @param other                         The data-dataset to compare to.
 * @throws DifferentColumnStructure     If the columns differ.
 * @throws DifferentRowStructure        If the number of rows differ.
 */
inline infix fun DataBatch<*>.mustHaveEquivalentStructureTo(other : DataBatch<*>) {
    this mustHaveEquivalentColumnStructureTo other
    this mustHaveEquivalentRowStructureTo other
}

// endregion

// region Row Structure

/**
 * Whether the row-structure of this structure is the same
 * as another's.
 *
 * @param other     The other structure with indexable rows.
 * @return          True if the structures have the same number of
 *                  rows, otherwise false.
 */
inline infix fun WithIndexableRows<*>.hasEquivalentRowStructureTo(other : WithIndexableRows<*>) = numRows == other.numRows

/**
 * Whether the row-structure of this structure is different
 * to another's.
 *
 * @param other     The other structure with indexable rows.
 * @return          False if the structures have the same number of
 *                  rows, otherwise true.
 */
inline infix fun WithIndexableRows<*>.doesNotHaveEquivalentRowStructureTo(other : WithIndexableRows<*>) = !(this hasEquivalentRowStructureTo other)

/**
 * Ensures that the row-structure of some other structure is the
 * same as this structure's.
 *
 * @param other                     The other structure with indexable rows.
 * @throws DifferentRowStructure    If the row-structures are different.
 */
inline infix fun WithIndexableRows<*>.mustHaveEquivalentRowStructureTo(other : WithIndexableRows<*>) {
    if (this doesNotHaveEquivalentRowStructureTo other)
        throw DifferentRowStructure()
}

// endregion

// region Column Structure

/**
 * Fast check to compare equality of headers.
 *
 * @param other
 *          The other set of headers to compare to this one.
 * @return
 *          Whether the two headers are equal.
 */
inline infix fun DataColumnHeaders.isEquivalentTo(other: DataColumnHeaders): Boolean {
    return identityToken === other.identityToken
}

/**
 * Checks if the two structures have the same columns in the same order.
 *
 * @param other     The other structure to compare to.
 * @return          True if the columns are the same, false if not.
 */
inline infix fun WithColumns.hasEquivalentColumnStructureTo(other : WithColumns) : Boolean {
    return headers isEquivalentTo other.headers
}

/**
 * Checks if the two structures do not have the same columns in the same order.
 *
 * @param other     The other structure to compare to.
 * @return          False if the columns are the same, true if not.
 */
inline infix fun WithColumns.doesNotHaveEquivalentColumnStructureTo(other : WithColumns) = !(this hasEquivalentColumnStructureTo other)

/**
 * Ensures the given structure has the same columns as this structure.
 *
 * @param other                         The other structure to compare to.
 * @throws DifferentColumnStructure     If the columns aren't the same.
 */
inline infix fun WithColumns.mustHaveEquivalentColumnStructureTo(other : WithColumns) {
    if (this doesNotHaveEquivalentColumnStructureTo other)
        throw DifferentColumnStructure()
}

// endregion

// region Representation

/**
 * TODO
 */
inline infix fun DataRepresentation<*, *, *>.isNotEquivalentTo(
    other: DataRepresentation<*, *, *>
): Boolean {
    return !(this isEquivalentTo other)
}

/**
 * TODO: Comment
 */
inline fun <T> DataType<*, *>.getEquivalentRepresentation(
    other: DataRepresentation<*, *, T>
): DataRepresentation<*, *, T>? {
    val representationIndex = other.representationIndex
    if (!indexInRange(representationIndex, representations.size)) return null
    val positionallyEquivalentRepresentation = representations[representationIndex]
    if (positionallyEquivalentRepresentation isNotEquivalentTo other) return null
    return positionallyEquivalentRepresentation as DataRepresentation<*, *, T>
}

// endregion
