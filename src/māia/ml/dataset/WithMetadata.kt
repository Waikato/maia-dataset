package māia.ml.dataset

/**
 * Interface for data-sets which have meta-data associated with them
 * (all data-sets implement this interface).
 */
interface WithMetadata {

    /** The meta-data for the data-set. */
    val metadata: DataMetadata

    /** Short-cut to the name of the data-set. */
    val name : String
        get() = metadata.name

}
