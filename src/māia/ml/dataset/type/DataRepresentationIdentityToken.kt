package māia.ml.dataset.type

import māia.util.superClassesUpTo
import java.lang.ref.WeakReference
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties

/**
 * Caches equivalency between data-representations, such that an equivalency
 * check becomes a check to see if they hold the same token.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class DataRepresentationIdentityToken private constructor(
    private val cls: KClass<*>,
    private val property : KProperty<*>
) {

    override fun equals(other : Any?) : Boolean {
        return other is DataRepresentationIdentityToken
                && other.cls == cls
                && other.property == property
    }

    override fun hashCode() : Int {
        var result = cls.hashCode()
        result = 31 * result + property.hashCode()
        return result
    }

    companion object Cache {
        /**
         * Gets the unique identity token for the given representation.
         *
         * @param target
         *          The representation that the token should identify.
         *
         * @return The identity token identifying the [target].
         */
        operator fun get(
            owner : DataType<*, *>,
            property : KProperty<*>
        ): DataRepresentationIdentityToken {
            // Search for the class which actually declares this property
            var cls: KClass<*> = owner::class
            while (property !in cls.declaredMemberProperties) {
                cls = cls.superClassesUpTo(DataType::class)[0]
            }

            // Need to create a token to see if an equivalent is in the cache
            val token = DataRepresentationIdentityToken(cls, property)

            return cache[token]?.get()
                ?: token.also { cache[it] = WeakReference(it) }
        }

        /** The cache of known identity tokens. */
        private val cache = WeakHashMap<DataRepresentationIdentityToken, WeakReference<DataRepresentationIdentityToken>>()

    }

}
