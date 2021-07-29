package māia.ml.dataset.error

/**
 * Error for when two columns must have the same header but do not.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class DifferentColumnHeader : Exception("Column headers not equivalent")
