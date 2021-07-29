package māia.ml.dataset.error

/**
 * Error for when two structures must have the same row-structure
 * but do not.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class DifferentRowStructure : Exception("Row structures are not equivalent")
