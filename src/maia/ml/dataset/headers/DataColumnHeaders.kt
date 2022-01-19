package maia.ml.dataset.headers

import maia.ml.dataset.WithColumns
import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.type.DataRepresentation

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
sealed interface DataColumnHeaders:
    List<DataColumnHeader>,
    Map<String, DataColumnHeader>,
    WithColumns
{

    /** The token which uniquely identifies these headers. */
    val identityToken: HeadersIdentityToken

    /**
     * Gets the equivalent [representation] as owned by this set of headers.
     *
     * @param representation
     *          The [DataRepresentation] that we should find.
     *
     * @return Our own equivalent representation, or null if we don't have one.
     */
    fun <T> ownedEquivalent(
        representation : DataRepresentation<*, *, T>
    ): DataRepresentation<*, *, T>?

    // Present so that delegation also picks these up from source
    override fun equals(other : Any?) : Boolean
    override fun hashCode() : Int
    override fun toString() : String
}
