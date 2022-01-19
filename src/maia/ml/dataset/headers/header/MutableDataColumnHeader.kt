package maia.ml.dataset.headers.header

import maia.ml.dataset.headers.MutableDataColumnHeaders
import maia.ml.dataset.type.DataType

/**
 * A [DataColumnHeader] that is owned by a [MutableDataColumnHeaders].
 */
class MutableDataColumnHeader internal constructor(
    internal val owner: MutableDataColumnHeaders,
    index: Int,
    name: String,
    type : DataType<*, *>,
    isTarget: Boolean
): DataColumnHeader() {

    override var index: Int = index
        internal set(value) {
            field = value
            identityToken = HeaderIdentityToken[this]
            type.updateRepresentations(value)
        }

    override var name : String = name
        internal set(value) {
            field = value
            identityToken = HeaderIdentityToken[this]
            type.updateRepresentations(value)
        }

    override val type by type

    override var isTarget : Boolean = isTarget
        internal set(value) {
            field = value
            identityToken = HeaderIdentityToken[this]
            type.updateRepresentations(value)
        }

    override var identityToken : HeaderIdentityToken = HeaderIdentityToken[this]
        set(value) {
            field = value
            type.updateRepresentations(value)
        }

    init {
        // Update the representations with the initial values
        this.type.updateRepresentations(owner)
        this.type.updateRepresentations(identityToken)
        this.type.updateRepresentations(index)
        this.type.updateRepresentations(name)
        this.type.updateRepresentations(isTarget)
    }
}
