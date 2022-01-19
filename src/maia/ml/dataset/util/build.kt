package maia.ml.dataset.util

/*
 * Module for building data-rows programmatically.
 *
 * TODO: Reinstate.


import māia.util.ElementIterator
import māia.util.lambda
import māia.ml.dataset.DataRow
import māia.ml.dataset.WithColumns

/**
 * Builds a data-row using the provided functions. Doesn't check the values
 * for type-conformance.
 *
 * @param size
 *          The number of columns to include in the data-row.
 * @param headerSupplier
 *          A function from column-index to column header.
 * @param dataSupplier
 *          A function from column-index to data-value.
 * @param cacheHeaders
 *          Whether to cache the headers at build time rather than retaining
 *          the [headerSupplier].
 * @param cacheData
 *          Whether to cache the data at build time rather than retaining
 *          the [dataSupplier].
 * @param externalTypedData
 *          Whether the data supplier is supplying data in the external type(s)
 *          or internal type(s) for the headers.
 * @return
 *          The built data-row.
 */
fun buildRow(
    size : Int,
    headerSupplier: (Int) -> DataColumnHeader,
    dataSupplier : (Int) -> Any?,
    cacheHeaders: Boolean = true,
    cacheData: Boolean = true,
    externalTypedData: Boolean = false
) : DataRow {
    val headerCache = if (cacheHeaders) Array(size, headerSupplier) else null

    val headerSupplierActual = if (headerCache !== null) headerCache::get else headerSupplier

    val headerIteratorSupplier = if (headerCache !== null) headerCache::iterator else lambda { ElementIterator(headerSupplier, size) }

    val internalDataSupplier = if (externalTypedData) { index -> headerSupplier(index).type.convertToInternalUnchecked(dataSupplier(index))} else dataSupplier

    val dataCache = if (cacheData) Array(size, internalDataSupplier) else null

    val dataSupplierActual = if (dataCache !== null) dataCache::get else internalDataSupplier

    val dataIteratorSupplier = if (dataCache !== null) dataCache::iterator else lambda { ElementIterator(internalDataSupplier, size) }

    return object : DataRow {
        override val numColumns : Int = size
        override fun getColumnHeader(columnIndex : Int) : DataColumnHeader = headerSupplierActual(columnIndex)
        override fun getColumn(columnIndex : Int) : Any? = dataSupplierActual(columnIndex)
        override fun iterateColumns() : Iterator<Any?> = dataIteratorSupplier()
        override fun iterateColumnHeaders() : Iterator<DataColumnHeader> = headerIteratorSupplier()
        override fun toString() : String = formatStringSimple()
    }
}

/**
 * Generates a data-row for the given headers using the
 * provided block of code.
 *
 * @receiver
 *          The columned data-structure to generate a row for.
 * @param cacheHeaders
 *          Whether to cache the headers at build time rather than indirecting
 *          to the receiver.
 * @param cacheData
 *          Whether to cache the data at build time rather than retaining the
 *          [block].
 * @param externalTypedData
 *          Whether the data supplier is supplying data in the external type(s)
 *          or internal type(s) for the headers.
 * @param block
 *          A function from (index, header) to the value for that position
 *          in the row.
 * @return
 *          The generated data-row.
 */
inline fun WithColumns.buildRow(
    cacheHeaders: Boolean = true,
    cacheData : Boolean = true,
    externalTypedData : Boolean = false,
    noinline block : (Int) -> Any?
) : DataRow {
    return buildRow(
        numColumns,
        this::getColumnHeader,
        block,
        cacheHeaders,
        cacheData,
        externalTypedData
    )
}

inline fun WithColumnHeaders.buildRow(
    noinline block : (Int) -> Any?
) : DataRow {
    return buildRow(Array(numColumns, block))
}

inline fun WithColumnHeaders.buildRow(
    data: Array<out Any?>
) : DataRow {
    return object : DataRow, WithColumnHeaders by this {
        private val dataCache = data
        override fun getColumn(columnIndex : Int) : Any? = dataCache[columnIndex]
        override fun iterateColumns() : Iterator<Any?> = dataCache.iterator()
    }
}

/**
 * Generates a data-row for the given headers using the
 * provided block of code.
 *
 * @receiver
 *          The columned data-structure to generate a row for.
 * @param cacheHeaders
 *          Whether to cache the headers at build time rather than indirecting
 *          to the receiver.
 * @param cacheData
 *          Whether to cache the data at build time rather than retaining the
 *          [block].
 * @param externalTypedData
 *          Whether the data supplier is supplying data in the external type(s)
 *          or internal type(s) for the headers.
 * @param block
 *          A function from (index, header) to the value for that position
 *          in the row.
 * @return
 *          The generated data-row.
 */
inline fun WithColumnHeaders.buildRow(
    cacheHeaders: Boolean = true,
    cacheData : Boolean = true,
    externalTypedData : Boolean = false,
    crossinline block : (Int, DataColumnHeader) -> Any?
) : DataRow {
    return buildRow(cacheHeaders, cacheData, externalTypedData) { index ->
        block(index, getColumnHeader(index))
    }
}

/**
 * Creates a cache of the current state of a data-row.
 *
 * @receiver    The row to cache.
 * @return      The row-cache.
 */
inline fun DataRow.cache() : DataRow {
    return buildRow(block = this::getColumn)
}
*/
