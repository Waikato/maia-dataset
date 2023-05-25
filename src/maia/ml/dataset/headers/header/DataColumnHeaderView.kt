package maia.ml.dataset.headers.header

import maia.ml.dataset.headers.DataColumnHeadersView
import maia.ml.dataset.type.DataType

/**
 * Allows viewing a header from with a [DataColumnHeadersView] with changed
 * index/name/target status.
 */
class DataColumnHeaderView internal constructor(
    internal val source: DataColumnHeader,
    internal val owner: DataColumnHeadersView?,
    override val index: Int = source.index,
    name: String = source.name,
    isTarget: Boolean = source.isTarget
): DataColumnHeader() {

    constructor(
        source: DataColumnHeader,
        index: Int = source.index,
        name: String = source.name,
        isTarget: Boolean = source.isTarget
    ): this(
        source,
        null,
        index,
        name,
        isTarget
    )

    override var name : String = name
        internal set(value) {
            field = value
            identityToken = HeaderIdentityToken[this]
        }

    override val type : DataType<*, *> = source.type

    override var isTarget : Boolean = isTarget
        internal set(value) {
            field = value
            identityToken = HeaderIdentityToken[this]
        }

    override var identityToken = HeaderIdentityToken[this]
        private set
}
