package māia.ml.dataset.type

import māia.util.*
import māia.util.datastructure.OrderedSet
import māia.util.datastructure.buildOrderedSet
import java.math.BigInteger

/**
 * Base-class for implementations of nominal data-types, where externally
 * values must be one of a number of strings.
 *
 * @param categories    The categories in this nominal type.
 * @param I             The type used to represent values of this data-type in the data-set.
 */
abstract class Nominal<I> private constructor(
        private val categories : OrderedSet<String>
) : FiniteDataType<I, String>, OrderedSet<String> by categories {

    constructor(vararg categories : String) : this(buildOrderedSet { addAll(categories) }) {
        // Make sure there are at least two categories
        if (categories.size < 2)
            throw IllegalArgumentException("Must provide at least 2 categories for a nominal data-type (received ${categories.size}")

        // Make sure the provided categories are unique
        val duplicateCategories = duplicates(categories.iterator())
        if (duplicateCategories.isNotEmpty())
            throw IllegalArgumentException("Duplicate categories provided: ${duplicateCategories.joinToString()}")
    }

    /** The number of nominal categories. */
    val numCategories : Int
        get() = size

    /** The range of valid indices for the categories. */
    val categoryIndices: IntRange = 0 until categories.size

    /**
     * Whether the given string is a category of this nominal type.
     *
     * @param category  The string to test for.
     * @return          True if the string is a category, false if not.
     */
    fun isCategory(category : String) : Boolean {
        return category in this
    }

    /**
     * Gets the ordered index of the given category as represented internally.
     *
     * @param category
     *          The category to get the index of.
     * @return
     *          The index of the category in the type's order.
     * @throws Exception
     *          If the category is not valid for this type.
     */
    open fun indexOfInternal(category : I) : Int {
        return indexOf(convertToExternal(category))
    }

    /**
     * Converts the index of a category to the internal representation.
     *
     * @param index
     *          The index of the category to convert.
     * @return
     *          The internal representation of the category at that index.
     * @throws IndexOutOfBoundsException
     *          If [index] isn't in [0, [numCategories]).
     */
    open fun convertIndexToInternal(index: Int): I {
        return convertToInternal(this[index])
    }

    /**
     * Iterates over the categories of this type.
     *
     * @return  An iterator over the categories in order.
     */
    fun iterateCategories() : Iterator<String> {
        return iterator()
    }

    // The number of categories is the number of possible values
    // for a nominal type
    final override val entropy : BigInteger = categories.size.toBigInteger()

    final override fun select(selection : BigInteger) : String {
        return this[selection.intValueExact()]
    }

    final override fun isValidExternal(value : String) : Boolean {
        return isCategory(value)
    }

    final override fun equals(other: Any?): Boolean {
        // A nominal type is equal to another if it has the same categories
        // in the same order
        return other is Nominal<*> && other.categories == categories
    }

    final override fun toString() : String {
        return iterateCategories().joinToString(
                separator = "', '",
                prefix = "Nominal('",
                postfix = "')"
        )
    }

    final override fun hashCode(): Int {
        return categories.hashCode()
    }

}
