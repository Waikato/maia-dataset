package maia.ml.dataset

import kotlinx.coroutines.flow.Flow

/**
 * Interface for structures which can return their constituent
 * rows in an iteration order.
 *
 * @param R     The type of row returned by the structure.
 */
interface WithAsyncRows<out R> {

    /**
     * Gets an iterator over the rows of the structure.
     *
     * @return  The row iterator.
     */
    fun rowFlow() : Flow<R>

}
