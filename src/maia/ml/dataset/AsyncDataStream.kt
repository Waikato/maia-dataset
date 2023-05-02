package maia.ml.dataset

/**
 * Base interface for data-sets which supply their data asynchronously from an
 * online source i.e. each row of data is supplied in order but
 * can't be revisited once supplied.
 *
 * @param R     The type of row returned by the structure.
 */
interface AsyncDataStream<out R : DataRow> :
    WithMetadata,
    WithColumns,
    WithAsyncRows<R>
