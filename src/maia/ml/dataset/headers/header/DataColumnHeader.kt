package maia.ml.dataset.headers.header

import maia.ml.dataset.type.DataType

/**
 * The base class for a single header to a column in a dataset.
 *
 * A header describes the data in a column of a data-set, including:
 *  - the index of the column.
 *  - the name of the column.
 *  - the [type][DataType] of data in the column.
 *  - whether the column is feature data or target data.
 *
 * This base class determines equality between headers, and provides an
 * ["identity token"][HeaderIdentityToken], which allows for fast comparison
 * between headers for equivalency.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
sealed class DataColumnHeader {

    /**
     * The index of the column. The value is controlled by the
     * [DataColumnHeaders] object that owns this header.
     */
    abstract val index: Int

    /** The name of the column. */
    abstract val name : String

    /** The type of data in the column. */
    abstract val type: DataType<*, *>

    /** Whether this column is target data (false is feature data). */
    abstract val isTarget : Boolean

    /** A token for fast equality checks. */
    abstract val identityToken: HeaderIdentityToken

    /**
     * String-representation of the header's target status.
     * T = target, F = feature.
     */
    val targetString: String
        get() = if (isTarget) "T" else "F"

    /**
     * Fast check to compare equality of headers.
     *
     * @param other
     *          The other header to compare to this one.
     * @return
     *          Whether the two headers are equal.
     */
    inline infix fun isEquivalentTo(other: DataColumnHeader): Boolean {
        return identityToken === other.identityToken
    }

    final override fun equals(other : Any?) : Boolean {
        return other === this ||
                other is DataColumnHeader &&
                other.index == this.index &&
                other.name == this.name &&
                other.type == this.type &&
                other.isTarget == this.isTarget
    }

    final override fun hashCode() : Int {
        var result = index.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + isTarget.hashCode()
        return result
    }

    final override fun toString() : String {
        return "$targetString $name@$index: $type"
    }
}
