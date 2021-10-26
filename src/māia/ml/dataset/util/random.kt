package māia.ml.dataset.util

import māia.ml.dataset.type.DataRepresentation
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
fun <D: DataRepresentation<D, FiniteDataType<*, *, *>, T>, T> D.random(
    source : Random = Random.Default
) : T {
    val repr = dataType.entropicRepresentation
    return convert(repr.random(source), repr)
}
