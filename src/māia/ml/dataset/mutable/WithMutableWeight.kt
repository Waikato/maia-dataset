package māia.ml.dataset.mutable

import māia.ml.dataset.WithWeight

/**
 * Interface for structures which have a relative weighting
 * among their peers, and that weight can be mutated.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
interface WithMutableWeight: WithWeight {
    override var weight : Double
}
