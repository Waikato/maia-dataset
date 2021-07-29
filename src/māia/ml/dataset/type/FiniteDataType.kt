package māia.ml.dataset.type

import java.math.BigInteger

/**
 * Interface for data-types which can only take one of a finite
 * number of values.
 *
 * @param I     The type used to represent values of this data-type in the data-set.
 * @param X     The type used to represent values of this data-type outside the data-set.
 */
interface FiniteDataType<I, X> : DataType<I, X> {

    /**
     * Defines the amount of information in a value of this type. Should be
     * equal to the number of possible values this data-type can take.
     */
    val entropy : BigInteger

    /**
     * Selects a value from this type.
     *
     * @param selection                     A value to generate a selection from.
     * @return                              A valid value for this data-type.
     * @throws IndexOutOfBoundsException    If the [selection] is not in [0, [entropy]).
     */
    fun select(selection : BigInteger) : X

}
