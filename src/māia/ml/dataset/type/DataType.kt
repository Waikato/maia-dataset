package māia.ml.dataset.type

/**
 * Represents the type of data in a column of a data-set.
 * All data-types must be immutable.
 *
 * @param I     The type used to represent values of this data-type in the data-set.
 * @param X     The type used to represent values of this data-type outside the data-set.
 */
interface DataType<I, X> {

    /**
     * Provides a default value for this data-type,
     * used for initialising data.
     *
     * @return  A default value of this type.
     */
    fun initial(): I

    /**
     * Converts an external value to an internal value.
     *
     * @param value         The value to convert.
     * @throws Exception    If the value is invalid for this data-type.
     */
    fun convertToInternal(value : X) : I

    /**
     * Converts an internal value to an external value.
     *
     * @param value         The value to convert.
     * @throws Exception    If the value is invalid for this data-type.
     */
    fun convertToExternal(value : I) : X

    /**
     * TODO
     */
    fun isValidInternal(value : I) : Boolean

    /**
     * TODO
     */
    fun isValidExternal(value : X) : Boolean

    /*
     * All data-types must define what it means to be the same data-type,
     * so that data-set columns can be compared to one-another. This also
     * requires that a hashCode method is defined.
     */
    override fun equals(other : Any?) : Boolean
    override fun hashCode() : Int

    /*
     * Data-types should be able to identify themselves by string.
     */
    override fun toString() : String

}
