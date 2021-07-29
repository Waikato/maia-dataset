package māia.ml.dataset.type

import java.math.BigInteger

/**
 * Base class for numeric data-types.
 *
 * @param I
 *          The type used to represent values of this data-type in the data-set.
 */
abstract class Numeric<I> : FiniteDataType<I, Double> {

    companion object {
        /** The offset for converting the unsigned entropy into a signed Long. */
        private val ENTROPY_OFFSET = BigInteger.ONE.shiftLeft(Long.SIZE_BITS - 1)

        /** The entropy of a numeric value. */
        val ENTROPY: BigInteger = ENTROPY_OFFSET.shiftLeft(1)
    }

    final override val entropy : BigInteger = ENTROPY

    final override fun initial() : I = convertToInternal(0.0)

    final override fun isValidExternal(value : Double) : Boolean {
        return true
    }

    final override fun select(selection : BigInteger) : Double = Double.fromBits((selection - ENTROPY_OFFSET).longValueExact())

    final override fun toString() : String = "Numeric"
    final override fun equals(other : Any?) : Boolean = other is Numeric<*>
    final override fun hashCode() : Int = Numeric::class.hashCode()

}
