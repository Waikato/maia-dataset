package māia.ml.dataset.error

/**
 * Error for when two structures must have the same column
 * structure, but they don't.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class DifferentColumnStructure : Exception("Column structure not equivalent")
