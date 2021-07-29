package māia.ml.dataset.util

import māia.ml.dataset.type.FiniteDataType
import māia.util.nextBigInteger
import kotlin.random.Random

/**
 * Returns a random value from the set of allowed values
 * of this data-type.
 *
 * @param source    An optional source of randomness.
 * @return          A randomly-selected value of this type.
 */
fun <X> FiniteDataType<*, X>.random(source : Random = Random.Default) : X {
    return select(source.nextBigInteger(entropy))
}

/**
 * Returns a random value from the set of allowed values
 * of this data-type, in the internal representation.
 *
 * @param source    An optional source of randomness.
 * @return          A randomly-selected value of this type.
 */
fun <I> FiniteDataType<I, *>.randomInternal(source : Random = Random.Default) : I {
    return convertToInternalUnchecked(select(source.nextBigInteger(entropy)))
}
