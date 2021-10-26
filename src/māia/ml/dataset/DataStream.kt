package māia.ml.dataset


/**
 * Base interface for data-sets which supply their data from an
 * online source i.e. each row of data is supplied in order but
 * can't be revisited once supplied.
 *
 * @param R     The type of row returned by the structure.
 */
interface DataStream<out R : DataRow> :
        WithMetadata,
        WithColumns,
        WithRows<R>
