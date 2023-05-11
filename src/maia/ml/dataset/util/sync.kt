package maia.ml.dataset.util

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import maia.ml.dataset.AsyncDataStream
import maia.ml.dataset.DataMetadata
import maia.ml.dataset.DataRow
import maia.ml.dataset.DataStream
import maia.ml.dataset.headers.DataColumnHeaders
import maia.util.sync


/**
 * Converts an asynchronous data-stream into a synchronous one.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
fun <R : DataRow> AsyncDataStream<R>.sync(
    capacity: Int = Channel.RENDEZVOUS,
    onBufferOverflow: BufferOverflow = BufferOverflow.SUSPEND
): DataStream<R> {
    // Check if it's already a synchronous stream
    if (this is DataStream<R>) return this

    return object : DataStream<R> {
        override val headers : DataColumnHeaders
            get() = this@sync.headers

        override val metadata : DataMetadata
            get() = this@sync.metadata

        override fun rowIterator() : Iterator<R> = this@sync.rowFlow().sync(capacity, onBufferOverflow)
    }
}
