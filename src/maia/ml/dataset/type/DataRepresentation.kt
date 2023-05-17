package maia.ml.dataset.type

import maia.ml.dataset.headers.MutableDataColumnHeaders
import maia.ml.dataset.error.InvalidValue
import maia.ml.dataset.error.UnownedRepresentationError
import maia.ml.dataset.headers.header.HeaderIdentityToken
import maia.ml.dataset.util.getEquivalentRepresentation
import maia.util.ensure
import maia.util.pass
import maia.util.property.SingleUseReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Base-class for all data representation types. A data representation contains
 * the necessary information to represent a value from a column as a particular
 * type. Also includes acceleration for determining whether a representation is
 * valid for a given set of headers, getting equivalent representations, etc.
 *
 * @param Self The this-type of the representation.
 * @param D The type of data-type that can own this representation.
 * @param T The Kotlin type used to represent values.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
abstract class DataRepresentation<
        Self: DataRepresentation<Self, D, T>,
        D: DataType<D, *>,
        T
>: SingleUseReadOnlyProperty<DataType<D, *>, Self>() {

    // Local cache of header properties for fast retrieval

    /** The index of this representation in the owning headers. */
    var columnIndex: Int = -1
        internal set

    /** The name of this representation in the owning headers. */
    var columnName: String = ""
        internal set

    /** The data-type of this representation in the owning headers. */
    val dataType: D
        get() = owner as D

    /** The target status of this representation in the owning headers. */
    var isTarget: Boolean = false
        internal set

    /** The ownership token of the headers that owns this representation. */
    var ownershipToken: MutableDataColumnHeaders.OwnershipToken? = null
        internal set

    /** The header-identity token of the headers that owns this representation. */
    var headerIdentityToken: HeaderIdentityToken? = null
        internal set

    /** The index of this representation in the owning data-type's list. */
    var representationIndex: Int = -1
        internal set

    /** The identity token representing equivalent data-representations. */
    var identityToken: DataRepresentationIdentityToken? = null
        internal set

    /**
     * Checks if a value using this representation is valid for the data-type.
     *
     * @param value The value to check.
     *
     * @return Whether the [value] is valid.
     */
    abstract fun isValid(value: T): Boolean

    /**
     * Gets an arbitrary initial value in this representation.
     *
     * @return Any valid value for this data-type, in this representation.
     */
    abstract fun initial(): T

    /**
     * Representations should implement this to provide conversion to other
     * representations. Will only be called with representations owned by
     * the data-type that owns this representation.
     *
     * @param value The value to convert.
     * @param fromRepresentation The representational-type of the given [value].
     *
     * @return The [value] converted to this representation.
     */
    protected abstract fun <I> convertValue(
        value: I,
        fromRepresentation : DataRepresentation<*, D, I>
    ): T

    /**
     * Converts a value from another representation into this one.
     *
     * @param value The value to convert.
     * @param fromRepresentation The representational-type of the given [value].
     *
     * @return The [value] converted to this representation.
     */
    fun <I> convert(
        value: I,
        fromRepresentation : DataRepresentation<*, *, I>
    ): T {
        // Make sure our data-type has an equivalent representation to the one provided
        val equivalent = dataType.getEquivalentRepresentation(fromRepresentation)
        if (equivalent === null)
            throw UnownedRepresentationError(fromRepresentation, dataType.header.owner)

        return convertValue(value, equivalent as DataRepresentation<*, D, I>)
    }

    /**
     * Checks if the [other] representation is equivalent to this one. Two
     * representations are equivalent when they are declared as the same property
     * on the same [DataType].
     *
     * @param other The other representation to compare to this one.
     *
     * @return Whether the representations are considered equivalent.
     */
    inline infix fun isEquivalentTo(other: DataRepresentation<*, *, *>): Boolean {
        return identityToken === other.identityToken
    }

    /**
     * Ensures a given value is
     */
    inline fun validate(value: T) = ensure(
        isValid(value),
        { throw InvalidValue(this, value) },
        ::pass
    )

    final override fun getValue() : Self = this as Self

    final override fun onDelegation(
        owner : DataType<D, *>,
        property : KProperty<*>,
        name : String
    ) {
        // Register with the oening data-type
        representationIndex = owner.register(this)

        // Generate our identity token
        identityToken = DataRepresentationIdentityToken[owner, property]
    }

    final override fun equals(other : Any?) : Boolean {
        return other === this
    }

    final override fun hashCode() : Int = super.hashCode()

    final override fun toString() : String {
        return if (isDelegated)
            "$name of ${owner::class.qualifiedName ?: "Unknown Datatype Class"}"
        else
            this::class.qualifiedName ?: "Unknown DataRepresentation"
    }
}
