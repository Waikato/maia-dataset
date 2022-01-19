package maia.ml.dataset.type

import maia.util.nextBigInteger
import java.math.BigInteger
import kotlin.random.Random


/**
 * Base-class for entropic representations of [FiniteDataType]s. This representation
 * presents values as a [BigInteger] in the range [0, [FiniteDataType.entropy]),
 * which uniquely maps to one of the finitely-many values in the data-type.
 *
 * @param Self See [DataRepresentation].
 * @param N The type of [Nominal] that owns this representation.
 */
abstract class EntropicRepresentation<
        Self: EntropicRepresentation<Self, D>,
        D: FiniteDataType<D, *, out Self>
> : DataRepresentation<Self, D, BigInteger>() {
    /**
     * Produces a random value of this data-type, in its
     * entropic representation.
     *
     * @param source An optional source of randomness.
     *
     * @return A [BigInteger] in [0, [FiniteDataType.entropy]), selecting
     *         a random value of this data-type.
     */
    fun random(source : Random = Random.Default): BigInteger =
        source.nextBigInteger(dataType.entropy)

    final override fun isValid(value : BigInteger) : Boolean =
        value >= BigInteger.ZERO && value < dataType.entropy

    final override fun initial() : BigInteger =
        BigInteger.ZERO
}

/**
 * Base-class for data-types which can only take one of a finite number of values.
 *
 * @param canonicalRepresentation See [DataType].
 * @param entropicRepresentation The entropic representation to use for this instance.
 * @param supportsMissingValues See [DataType].
 * @param entropy The number of possible values this data-type can take.
 *
 * @param Self See [DataType].
 * @param C See [DataType].
 * @param E The type of entropic representation this data-type uses.
 */
abstract class FiniteDataType<
        Self: FiniteDataType<Self, C, E>,
        C: DataRepresentation<C, Self, *>,
        E: EntropicRepresentation<E, Self>
>(
    canonicalRepresentation: C,
    entropicRepresentation : E,
    supportsMissingValues: Boolean,
    val entropy: BigInteger
) : DataType<Self, C>(
    canonicalRepresentation,
    supportsMissingValues
) {

    /**
     * Represents each unique value in this datatype as an integer
     * in [0, [entropy]).
     */
    val entropicRepresentation: E by entropicRepresentation
}
