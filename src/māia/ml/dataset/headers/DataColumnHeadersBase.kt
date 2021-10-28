package māia.ml.dataset.headers

import māia.util.all
import māia.util.indexIterator

/**
 * Common functionality for all implementations of [DataColumnHeaders].
 */
sealed class DataColumnHeadersBase: DataColumnHeaders {

    /** Keeps track of the mapping from a header's name to its index for fast lookup by name. */
    internal abstract val nameToIndexMap: Map<String, Int>

    /** The names of the headers, in order. */
    internal abstract val names: List<String>

    @Suppress("OVERRIDE_BY_INLINE")
    final override inline val headers : DataColumnHeaders get() = this

    final override fun equals(
        other : Any?
    ) : Boolean {
        return other === this ||
                other is DataColumnHeaders &&
                other.size == size &&
                indexIterator(size).all { other[it] isEquivalentTo this[it] }
    }

    final override fun hashCode() : Int {
        var result = 0
        for (header in this) {
            result = 31 * result + header.hashCode()
        }
        return result
    }

    final override fun toString() : String {
        return joinToString(
            prefix = "DataColumnHeaders[", postfix = "]"
        ) {
            "${it.targetString}@${it.index}: \"${it.name}\" ${it.type}"
        }
    }

}

