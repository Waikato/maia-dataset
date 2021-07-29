package māia.ml.dataset

/**
 * Interface for structures which have a relative weighting
 * among their peers.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
interface WithWeight {
    /** The weight of this object. */
    val weight: Double
}
