package māia.ml.dataset

/**
 * Interface for structures which can return their constituent
 * rows in an iteration order.
 *
 * @param R     The type of row returned by the structure.
 */
interface WithRows<out R> {

    /**
     * Gets an iterator over the rows of the structure.
     *
     * @return  The row iterator.
     */
    fun rowIterator() : Iterator<R>

}
