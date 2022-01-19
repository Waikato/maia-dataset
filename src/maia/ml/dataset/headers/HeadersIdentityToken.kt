package maia.ml.dataset.headers

import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.headers.header.HeaderIdentityToken
import maia.ml.dataset.type.DataRepresentation
import maia.util.RandomAccessListIterator
import maia.util.RandomAccessSubList
import maia.util.map
import java.lang.ref.WeakReference
import java.util.*
import kotlin.collections.HashMap

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
class HeadersIdentityToken private constructor(
    vararg val headerTokens: HeaderIdentityToken
): DataColumnHeadersBase() {

    override val names : List<String> = headerTokens.map { it.name }

    override val nameToIndexMap : Map<String, Int> = HashMap<String, Int>().apply { names.forEachIndexed { index, name -> set(name, index) } }

    override val identityToken : HeadersIdentityToken = this

    override fun <T> ownedEquivalent(
        representation : DataRepresentation<*, *, T>
    ) : DataRepresentation<*, *, T>? = getEquivalentRepresentation(representation)

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
            target: DataColumnHeaders
        ): HeadersIdentityToken {
            return cache[target]?.get()
                ?: HeadersIdentityToken(
                    *Array(target.size) { target[it].identityToken }
                ).also { cache[it] = WeakReference(it) }
        }

        /** The cache of known identity tokens. */
        private val cache = WeakHashMap<DataColumnHeaders, WeakReference<HeadersIdentityToken>>()
    }

    // List/Map methods
    override fun contains(element : DataColumnHeader) : Boolean = element.index.let { it < size && element isEquivalentTo this[it] }
    override fun containsAll(elements : Collection<DataColumnHeader>) : Boolean = elements.all { contains(it) }
    override fun get(key : String) : DataColumnHeader? = nameToIndexMap[key]?.let { this[it] }
    override fun indexOf(element : DataColumnHeader) : Int = if (contains(element)) element.index else -1
    override fun isEmpty() : Boolean = size == 0
    override fun lastIndexOf(element : DataColumnHeader) : Int = if (contains(element)) element.index else -1
    override fun iterator() : Iterator<DataColumnHeader> = listIterator(0)
    override fun listIterator() : ListIterator<DataColumnHeader> = listIterator(0)
    override fun containsKey(key : String) : Boolean = key in nameToIndexMap
    override fun containsValue(value : DataColumnHeader) : Boolean = contains(value)
    override val size : Int = headerTokens.size
    override fun get(index : Int) : DataColumnHeader = headerTokens[index]
    override fun listIterator(index : Int) : ListIterator<DataColumnHeader> = RandomAccessListIterator(this, index)
    override fun subList(fromIndex : Int, toIndex : Int) : List<DataColumnHeader> = RandomAccessSubList(this, fromIndex, toIndex)
    override val keys : Set<String> = nameToIndexMap.keys
    override val values : Collection<DataColumnHeader> = object : Collection<DataColumnHeader> by this {}
    override val entries : Set<Map.Entry<String, DataColumnHeader>> = object : Set<Map.Entry<String, DataColumnHeader>> {
        override val size : Int = this@HeadersIdentityToken.size
        override fun contains(element : Map.Entry<String, DataColumnHeader>) : Boolean = this@HeadersIdentityToken.contains(element.value) && element.key == element.value.name
        override fun containsAll(elements : Collection<Map.Entry<String, DataColumnHeader>>) : Boolean = elements.all { contains(it) }
        override fun isEmpty() : Boolean = size == 0
        override fun iterator() : Iterator<Map.Entry<String, DataColumnHeader>> = this@HeadersIdentityToken.iterator().map {
            object : Map.Entry<String, DataColumnHeader> {
                override val key : String = it.name
                override val value : DataColumnHeader = it
            }
        }
    }
}
