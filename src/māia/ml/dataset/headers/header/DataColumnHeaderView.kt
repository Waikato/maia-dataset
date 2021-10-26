package māia.ml.dataset.headers.header

import māia.ml.dataset.headers.DataColumnHeadersView
import māia.ml.dataset.type.DataType

class DataColumnHeaderView internal constructor(
    internal val source: DataColumnHeader,
    internal val owner: DataColumnHeadersView?,
    index: Int = source.index,
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

    override val index = index

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

    init {
        identityToken = HeaderIdentityToken[this]
    }
}
