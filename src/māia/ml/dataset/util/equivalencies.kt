package māia.ml.dataset.util

/*
 * Provides methods for checking is data-structures are equivalent to one-another.
 */

import māia.ml.dataset.*
import māia.ml.dataset.error.*
import māia.util.all
import māia.util.zip

// region Type
/**
 * Whether this column has the same type of data as another
 * column.
 *
 * @param other     The column to compare type with.
 * @return          True if the columns both have the same type,
 *                  false otherwise.
 */
infix fun DataColumnHeader.isSameTypeAs(other : DataColumnHeader) = type == other.type

/**
 * Whether this column does not have the same type of data as another
 * column.
 *
 * @param other     The column to compare type with.
 * @return          False if the columns both have the same type,
 *                  true otherwise.
 */
infix fun DataColumnHeader.isNotSameTypeAs(other : DataColumnHeader) = !(this isSameTypeAs other)

/**
 * Ensures that this column has the same type of data as another
 * column.
 *
 * @param other     The column to compare type with.
 * @throws DifferentDataType    If the column types aren't the same.
 */
infix fun DataColumnHeader.mustBeSameTypeAs(other : DataColumnHeader) {
    if (this isNotSameTypeAs other)
        throw DifferentDataType()
}

// endregion

// region Header

/**
 * Whether this column header describes the same column as
 * another.
 *
 * @param other     The other column header.
 * @return          True if the column headers describe the same column,
 *                  false otherwise.
 */
infix fun DataColumnHeader.isEquivalentTo(other : DataColumnHeader) : Boolean {
    return name == other.name && type == other.type && isTarget == other.isTarget
}

/**
 * Whether this column header describes a different column to
 * another.
 *
 * @param other     The other column header.
 * @return          False if the column headers describe the same column,
 *                  true otherwise.
 */
infix fun DataColumnHeader.isNotEquivalentTo(other : DataColumnHeader) = !(this isEquivalentTo other)

/**
 * Ensures that this column header describes the same column as
 * another.
 *
 * @param other                     The other column header.
 * @throws DifferentColumnHeader    If the headers are not equivalent.
 */
infix fun DataColumnHeader.mustBeEquivalentTo(other : DataColumnHeader) {
    if (this isNotEquivalentTo other)
        throw DifferentColumnHeader()
}

// endregion

// region Structure

/**
 * Whether the structure of this column is equivalent in header and
 * size to another.
 *
 * @param other     The column to compare to.
 * @return          True if the structures are equivalent,
 *                  false if not.
 */
infix fun DataColumn.hasEquivalentStructureTo(other : DataColumn) : Boolean {
    return header isEquivalentTo other.header && this hasEquivalentRowStructureTo other
}

/**
 * Whether the structure of this column is different in header and/or
 * size to another.
 *
 * @param other     The column to compare to.
 * @return          False if the structures are equivalent,
 *                  true if not.
 */
infix fun DataColumn.doesNotHaveEquivalentStructureTo(other : DataColumn) = !(this hasEquivalentStructureTo other)

/**
 * Ensures the structure of this column is equivalent in header
 * and size to another.
 *
 * @param other                     The column to compare to.
 * @throws DifferentColumnHeader    If the headers differ.
 * @throws DifferentRowStructure    If the sizes differ.
 */
infix fun DataColumn.mustHaveEquivalentStructureTo(other : DataColumn) {
    header mustBeEquivalentTo other.header
    this mustHaveEquivalentRowStructureTo other
}


/**
 * Checks if the structure of this data-dataset is the same as that of
 * another.
 *
 * @param other     The data-dataset to compare to.
 * @return          True if the two data-sets have the same structure,
 *                  false if not.
 */
infix fun DataBatch<*, *>.hasEquivalentStructureTo(other : DataBatch<*, *>) : Boolean {
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
infix fun DataBatch<*, *>.doesNotHaveEquivalentStructureTo(other : DataBatch<*, *>) = !(this hasEquivalentStructureTo other)

/**
 * Ensures that the structure of this data-dataset is the same as that
 * of another.
 *
 * @param other                         The data-dataset to compare to.
 * @throws DifferentColumnStructure     If the columns differ.
 * @throws DifferentRowStructure        If the number of rows differ.
 */
infix fun DataBatch<*, *>.mustHaveEquivalentStructureTo(other : DataBatch<*, *>) {
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
infix fun WithIndexableRows<*>.hasEquivalentRowStructureTo(other : WithIndexableRows<*>) = numRows == other.numRows

/**
 * Whether the row-structure of this structure is different
 * to another's.
 *
 * @param other     The other structure with indexable rows.
 * @return          False if the structures have the same number of
 *                  rows, otherwise true.
 */
infix fun WithIndexableRows<*>.doesNotHaveEquivalentRowStructureTo(other : WithIndexableRows<*>) = !(this hasEquivalentRowStructureTo other)

/**
 * Ensures that the row-structure of some other structure is the
 * same as this structure's.
 *
 * @param other                     The other structure with indexable rows.
 * @throws DifferentRowStructure    If the row-structures are different.
 */
infix fun WithIndexableRows<*>.mustHaveEquivalentRowStructureTo(other : WithIndexableRows<*>) {
    if (this doesNotHaveEquivalentRowStructureTo other)
        throw DifferentRowStructure()
}

// endregion

// region Column Structure

/**
 * Checks if the two structures have the same columns in the same order.
 *
 * @param other     The other structure to compare to.
 * @return          True if the columns are the same, false if not.
 */
infix fun WithColumnHeaders.hasEquivalentColumnStructureTo(other : WithColumnHeaders) : Boolean {
    // Must have the same number of columns
    if (numColumns != other.numColumns) return false

    // Must be the same name/type in each column  position
    return zip(iterateColumnHeaders(), other.iterateColumnHeaders()).all { (c1, c2) ->
        c1 isEquivalentTo c2
    }
}

/**
 * Checks if the two structures do not have the same columns in the same order.
 *
 * @param other     The other structure to compare to.
 * @return          False if the columns are the same, true if not.
 */
infix fun WithColumnHeaders.doesNotHaveEquivalentColumnStructureTo(other : WithColumnHeaders) = !(this hasEquivalentColumnStructureTo other)

/**
 * Ensures the given structure has the same columns as this structure.
 *
 * @param other                         The other structure to compare to.
 * @throws DifferentColumnStructure     If the columns aren't the same.
 */
infix fun WithColumnHeaders.mustHaveEquivalentColumnStructureTo(other : WithColumnHeaders) {
    if (this doesNotHaveEquivalentColumnStructureTo other)
        throw DifferentColumnStructure()
}

// endregion
