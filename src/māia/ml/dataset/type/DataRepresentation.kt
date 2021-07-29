package māia.ml.dataset.type

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
interface DataRepresentation<I, X> {

    fun fromInternal(internal: I): X

    fun toInternal(external: X): I

    fun isValid(external: X): Boolean

}
