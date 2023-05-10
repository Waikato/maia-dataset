package maia.ml.dataset.type

import maia.ml.dataset.headers.header.HeaderIdentityToken
import maia.ml.dataset.headers.header.MutableDataColumnHeader
import maia.ml.dataset.headers.MutableDataColumnHeaders
import maia.util.datastructure.ReadOnlyArray
import maia.util.datastructure.readOnly
import maia.util.property.SingleUseReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * Represents the type of data in a column of a data-set.
 * All data-types must be immutable.
 *
 * @param canonicalRepresentation The canonical representation to use for this instance.
 * @param supportsMissingValues Whether this data-type can take missing values.
 *
 * @param Self The this-type of this data-type.
 * @param C The type of canonical representation this data-type uses.
 */
abstract class DataType<
        Self: DataType<Self, C>,
        C: DataRepresentation<C, Self, *>
>(
    canonicalRepresentation: C,
    val supportsMissingValues: Boolean
): SingleUseReadOnlyProperty<MutableDataColumnHeader, Self>() {

    /** All representations attached to the data-type. */
    var representations: ReadOnlyArray<out DataRepresentation<*, Self, *>> = emptyArray<DataRepresentation<*, Self, *>>().readOnly()
        private set

    /** The fully-fledged representation of a value in this data-type. */
    val canonicalRepresentation: C by canonicalRepresentation

    /** The header this datatype is assigned to. */
    val header: MutableDataColumnHeader
        get() = owner

    final override fun getValue() : Self = this as Self

    // Extra delegation functionality is optional to override
    override fun onDelegation(
        owner: MutableDataColumnHeader,
        property: KProperty<*>,
        name: String
    ) {}

    /**
     * Returns a copy of this data-type.
     */
    abstract fun copy(): Self

    /*
     * All data-types must define what it means to be the same data-type,
     * so that data-set columns can be compared to one-another. This also
     * requires that a hashCode method is defined.
     */
    abstract override fun equals(other : Any?) : Boolean
    abstract override fun hashCode() : Int

    /*
     * Data-types should be able to identify themselves by string.
     */
    abstract override fun toString() : String

    /**
     * Called by [DataRepresentation]s that attach to this data-type.
     *
     * @param representation
     *          The [DataRepresentation] being attached.
     */
    internal fun register(representation : DataRepresentation<*, Self, *>): Int {
        return representations.size.also { size ->
            representations = Array(size + 1) { index -> if (index < size) representations[index] else representation }.readOnly()
        }
    }

    internal fun updateRepresentations(index: Int) {
        representations.forEach { it.columnIndex = index }
    }

    internal fun updateRepresentations(name: String) {
        representations.forEach { it.columnName = name }
    }

    internal fun updateRepresentations(isTarget: Boolean) {
        representations.forEach { it.isTarget = isTarget }
    }

    internal fun updateRepresentations(owner: MutableDataColumnHeaders?) {
        representations.forEach { it.ownershipToken = owner?.ownershipToken }
    }

    internal fun updateRepresentations(headerIdentityToken : HeaderIdentityToken) {
        representations.forEach { it.headerIdentityToken = headerIdentityToken }
    }
}
