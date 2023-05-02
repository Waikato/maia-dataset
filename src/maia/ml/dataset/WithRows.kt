package maia.ml.dataset

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Interface for structures which can return their constituent
 * rows in an iteration order.
 *
 * @param R     The type of row returned by the structure.
 */
interface WithRows<out R>: WithAsyncRows<R> {

    /**
     * Gets an iterator over the rows of the structure.
     *
     * @return  The row iterator.
     */
    fun rowIterator() : Iterator<R>

    override fun rowFlow() : Flow<R> {
        // Iterator should be initialised at same time as flow
        val iterator = rowIterator()

        return flow {
            for (row in iterator) {
                emit(row)
            }
        }
    }

}
