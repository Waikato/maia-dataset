package maia.ml.dataset.headers.header

import maia.ml.dataset.type.DataType
import java.lang.ref.WeakReference
import java.util.*

/**
 * Token class which caches equality checks amongst headers. Each instance
 * represents a unique combination of ([index], [name], [type], [isTarget]),
 * and only one instance can exist for each combination. Therefore, headers
 * can hold onto the token representing their own settings for the
 * aforementioned attributes, and equality between headers boils down to
 * checking they hold the same token.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class HeaderIdentityToken private constructor(
    override val index: Int,
    override val name: String,
    override val type: DataType<*, *>,
    override val isTarget: Boolean
): DataColumnHeader() {

    @Suppress("OVERRIDE_BY_INLINE")
    override inline val identityToken : HeaderIdentityToken
        get() = this

    companion object Cache {
        /**
         * Gets the unique identity token for the given header.
         *
         * @param target
         *          The header that the token should represent.
         *
         * @return The identity token representing the [target].
         */
        operator fun get(
            target: DataColumnHeader
        ): HeaderIdentityToken {
            return cache[target]?.get()
                ?: HeaderIdentityToken(
                    target.index,
                    target.name,
                    target.type,
                    target.isTarget
                ).also { cache[it] = WeakReference(it) }
        }

        /** The cache of known identity tokens. */
        private val cache = WeakHashMap<HeaderIdentityToken, WeakReference<HeaderIdentityToken>>()
    }
}
