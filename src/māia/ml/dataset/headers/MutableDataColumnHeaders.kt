package māia.ml.dataset.headers

import māia.ml.dataset.headers.header.DataColumnHeader
import māia.ml.dataset.headers.header.MutableDataColumnHeader
import māia.ml.dataset.type.DataRepresentation
import māia.ml.dataset.type.DataType
import māia.util.doIfPresent
import māia.util.inlineRangeForLoop
import māia.util.map

/**
 * TODO
 */
class MutableDataColumnHeaders(
    initialCapacity: Int? = null
): MutableDataColumnHeadersBase() {

    /** Keeps track of the mapping from a header's name to its index for fast lookup by name. */
    override val nameToIndexMap: HashMap<String, Int> = initialCapacity?.let { HashMap(it) } ?: HashMap()

    /** The names of the headers, in order. */
    override val names: ArrayList<String> = initialCapacity?.let { ArrayList(it) } ?: ArrayList()

    /** The header objects that this set of headers owns. */
    internal val headersInternal: ArrayList<MutableDataColumnHeader> = initialCapacity?.let { ArrayList(it) } ?: ArrayList()

    /** Gets a read-only view of these headers. */
    val readOnlyView: DataColumnHeaders = MutableDataColumnHeadersReadOnlyView(this)

    // Initialise the identity token
    init { identityToken = HeadersIdentityToken[this] }

    fun set(index : Int, name: String, type: DataType<*, *>, isTarget: Boolean) {
        checkSet(index, name)
        setInternal(index, name, type, isTarget)
    }

    private fun setInternal(
        index : Int,
        name: String,
        type: DataType<*, *>,
        isTarget: Boolean
    ) = changeStructure {
        // Remove the old name
        nameToIndexMap.remove(names[index])

        // Remove the ownership of this header
        headersInternal[index].type.updateRepresentations(null)

        // Create and add a new header
        names[index] = name
        headersInternal[index] = createHeader(index, name, type, isTarget)
        nameToIndexMap[name] = index
    }

    fun checkDelete(name: String, index: Int? = null): Int {
        nameToIndexMap.doIfPresent(name) { indexActual ->
            if (index != null && index != indexActual)
                throw IllegalArgumentException("Header $name is at index $indexActual, not $index")
            return indexActual
        }

        throw IllegalArgumentException("Unknown header name: $name")
    }

    fun delete(name: String, index: Int? = null): Int {
        val indexActual = checkDelete(name,  index)
        deleteInternal(indexActual, name)
        return indexActual
    }

    fun checkDelete(index: Int, name: String? = null): String {
        if (index < 0 || index >= size) {
            throw IllegalArgumentException("Index must be in [0, $size), got $index")
        }

        return when (name) {
            null -> names[index]
            !in nameToIndexMap -> throw IllegalArgumentException("Unknown header name: $name")
            else -> {
                val nameAtIndex = names[index]
                if (nameAtIndex != name)
                    throw IllegalArgumentException("Header at index $index is named $nameAtIndex, not $name")
                name
            }
        }
    }

    fun delete(index: Int, name: String? = null): String {
        val nameActual = checkDelete(index, name)
        deleteInternal(index, nameActual)
        return nameActual
    }

    /**
     * Performs the actual removal of a header, after the public methods
     * have checked the [index] and [name] are valid.
     *
     * @param index
     *          The index of the header to remove.
     * @param name
     *          The name of the header to remove.
     */
    private fun deleteInternal(
        index: Int,
        name: String
    ) = changeStructure {
        // Remove the record keeping for the indicated header
        names.removeAt(index)
        headersInternal.removeAt(index).type.updateRepresentations(null)
        nameToIndexMap.remove(name)

        // Update the indices and any representation caches
        inlineRangeForLoop(index, names.size) {
            nameToIndexMap[names[it]] = it
            headersInternal[it].index = it
        }
    }

    fun checkInsert(index: Int, name: String) {
        // Can't double-up on header names
        if (name in nameToIndexMap) {
            throw IllegalArgumentException("Header name $name already in use")
        }

        // Must be a valid insertion index
        if (index < 0 || index > size) {
            throw IllegalArgumentException("Insert index must be in [0, $size], got $index")
        }
    }

    fun insert(
        index: Int,
        name: String,
        type: DataType<*, *>,
        isTarget : Boolean
    ) = changeStructure {
        checkInsert(index, name)
        names.add(index, name)
        headersInternal.add(
            index,
            createHeader(index, name, type, isTarget)
        )
        inlineRangeForLoop(index, names.size) {
            nameToIndexMap[names[it]] = it
            headersInternal[it].index = it
        }
    }

    fun checkAppend(name: String) = checkInsert(size, name)

    fun append(name: String, type: DataType<*, *>, isTarget : Boolean) = insert(size, name, type, isTarget)

    fun clear() = changeStructure {
        headersInternal.forEach { it.type.updateRepresentations(null) }
        headersInternal.clear()
        nameToIndexMap.clear()
    }

    /**
     * Token class which is used to quickly check for ownership of a
     * [DataRepresentation].
     */
    class OwnershipToken

    /** The ownership token for this set of headers. */
    val ownershipToken = OwnershipToken()

    @Suppress("OVERRIDE_BY_INLINE")
    override inline fun <T> ownedEquivalent(
        representation : DataRepresentation<*, *, T>
    ) : DataRepresentation<*, *, T>? {
        return if (representation.ownershipToken !== ownershipToken) {
            getEquivalentRepresentation(representation)
        } else {
            representation
        }
    }

    /**
     * Creates a new header owned by this set of headers.
     *
     * @param index
     *          The index at which to create the header.
     * @param name
     *          The name to give the header.
     * @param type
     *          The data-type of the header. Will use the given value directly
     *          if it is not already delegated, otherwise will create a copy.
     * @param isTarget
     *          Whether this header should be tagged as a target.
     * @return
     *          The created header.
     */
    internal fun createHeader(
        index: Int,
        name: String,
        type: DataType<*, *>,
        isTarget : Boolean
    ): MutableDataColumnHeader {
        return MutableDataColumnHeader(
            this,
            index,
            name,
            if (type.isDelegated) type.copy() else type,
            isTarget
        )
    }

    // List/Map method implementations

    override val size : Int get() = headersInternal.size
    override fun iterator() : Iterator<MutableDataColumnHeader> = headersInternal.iterator()
    override fun contains(element : DataColumnHeader) : Boolean = getEquivalentHeader(element) !== null
    override fun containsAll(elements : Collection<DataColumnHeader>) : Boolean = elements.all { contains(it) }
    override fun get(index : Int) : MutableDataColumnHeader = headersInternal[index]
    override fun indexOf(element : DataColumnHeader) : Int = getEquivalentHeader(element)?.index ?: -1
    override fun isEmpty() : Boolean = headersInternal.isEmpty()
    override fun lastIndexOf(element : DataColumnHeader) : Int = indexOf(element)
    override fun containsKey(key : String) : Boolean = key in nameToIndexMap
    override fun containsValue(value : DataColumnHeader) : Boolean = contains(value)
    override fun get(key : String) : MutableDataColumnHeader? = nameToIndexMap[key]?.let { headersInternal[it] }
    override fun listIterator() : ListIterator<MutableDataColumnHeader> = headersInternal.listIterator()
    override fun listIterator(index : Int) : ListIterator<MutableDataColumnHeader> = headersInternal.listIterator()
    override fun subList(fromIndex : Int, toIndex : Int) : List<MutableDataColumnHeader> = headersInternal.subList(fromIndex, toIndex)
    override val keys : Set<String> = object : Set<String> {
        override val size : Int = this@MutableDataColumnHeaders.size
        override fun contains(element : String) : Boolean = containsKey(element)
        override fun containsAll(elements : Collection<String>) : Boolean = elements.all { contains(it) }
        override fun isEmpty() : Boolean = size == 0
        override fun iterator() : Iterator<String> = this@MutableDataColumnHeaders.names.iterator()
    }
    override val values : Collection<MutableDataColumnHeader> = object : Collection<MutableDataColumnHeader> {
        override val size : Int = this@MutableDataColumnHeaders.size
        override fun contains(element : MutableDataColumnHeader) : Boolean = containsValue(element)
        override fun containsAll(elements : Collection<MutableDataColumnHeader>) : Boolean = elements.all { contains(it) }
        override fun isEmpty() : Boolean = size == 0
        override fun iterator() : Iterator<MutableDataColumnHeader> = this@MutableDataColumnHeaders.iterator()
    }
    override val entries : Set<Map.Entry<String, MutableDataColumnHeader>> = object : Set<Map.Entry<String, MutableDataColumnHeader>> {
        override val size : Int = this@MutableDataColumnHeaders.size
        override fun contains(element : Map.Entry<String, MutableDataColumnHeader>) : Boolean = containsValue(element.value) && element.key == element.value.name
        override fun containsAll(elements : Collection<Map.Entry<String, MutableDataColumnHeader>>) : Boolean = elements.all { contains(it) }
        override fun isEmpty() : Boolean = size == 0
        override fun iterator() : Iterator<Map.Entry<String, MutableDataColumnHeader>> = this@MutableDataColumnHeaders.iterator().map {
            object : Map.Entry<String, MutableDataColumnHeader> {
                override val key : String = it.name
                override val value : MutableDataColumnHeader = it
            }
        }
    }
}
