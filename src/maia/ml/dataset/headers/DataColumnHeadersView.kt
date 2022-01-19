package maia.ml.dataset.headers

import maia.ml.dataset.headers.header.DataColumnHeader
import maia.ml.dataset.headers.header.DataColumnHeaderView
import maia.ml.dataset.type.DataRepresentation
import maia.ml.dataset.util.translateColumn
import maia.util.datastructure.ConcurrentModificationManager
import maia.util.datastructure.OrderedSet
import maia.util.ensureHasNext
import maia.util.ensureHasPrevious
import maia.util.ensureIndexInRange
import maia.util.ensureSublistRange
import maia.util.indexInRange
import maia.util.map

/**
 * A read-only view of a mutable set of column headers.
 *
 * @param source
 *          The source set of column headers.
 */
class DataColumnHeadersView(
    private val source : DataColumnHeaders,
    private val columns : OrderedSet<Int>? = null
): MutableDataColumnHeadersBase() {

    init {
        columns?.let { columns ->
            columns.forEach {
                ensureIndexInRange(it, source.numColumns) {}
            }
        }
    }

    private val headersInternal = Array(columns?.size ?: source.size) {
        DataColumnHeaderView(this.source[translateColumn(columns, it)], it)
    }
    override val nameToIndexMap = HashMap<String, Int>(headersInternal.size)
    override val names = ArrayList<String>(headersInternal.size)

    /** Take a snapshot of our source when we were created, to invalidate this view
    when the source changes. */
    private val sourceSnapshot = when (source) {
        is MutableDataColumnHeadersBase -> source.concurrentModificationManager.View()
        is MutableDataColumnHeadersReadOnlyView -> source.source.concurrentModificationManager.View()
        is HeadersIdentityToken -> null
    }

    init {
        headersInternal.forEach { header ->
            nameToIndexMap[header.name] = header.index
            names.add(header.name)
        }

        // Initialise the identity token
        identityToken = HeadersIdentityToken[this]
    }

    /**
     * Helper for invalidating the view on structural changes to the [source].
     *
     * @param block
     *          The action to perform if there haven't been any changes.
     */
    internal inline fun <R> checkForSourceChanges(
        crossinline block: () -> R
    ): R = sourceSnapshot?.checkForStructuralModification(block) ?: block()

    /**
     * TODO
     *
     * @param block
     *          The action to perform if there haven't been any changes.
     */
    internal inline fun <R> ConcurrentModificationManager.View.checkForChanges(
        crossinline block: () -> R
    ): R = checkForStructuralModification(block)

    /**
     * TODO
     *
     * @param block
     *          The action to perform if there haven't been any changes.
     */
    internal inline fun <R> ConcurrentModificationManager.View.checkForAnyChanges(
        crossinline block: () -> R
    ): R = checkForStructuralModification { checkForSourceChanges(block) }

    override fun <T> ownedEquivalent(
        representation : DataRepresentation<*, *, T>
    ) : DataRepresentation<*, *, T>? = checkForSourceChanges {
        // Get the equivalent from our source
        val sourceEquivalent = source.ownedEquivalent(representation)
        // Also make sure the index of the representation is one we are viewing
        if (columns?.contains(representation.columnIndex) == false)
            null
        else
            sourceEquivalent
    }

    // List/Map method implementations

    override val size : Int get() = checkForSourceChanges { headersInternal.size }
    override fun iterator() : Iterator<DataColumnHeaderView> = listIterator()
    override fun contains(element : DataColumnHeader) : Boolean = element.index.let { indexInRange(it, size) && headersInternal[it] == element }
    override fun containsAll(elements : Collection<DataColumnHeader>) : Boolean = if (elements.isEmpty()) checkForSourceChanges { true } else elements.all { contains(it) }
    override fun get(index : Int) : DataColumnHeaderView = checkForSourceChanges { headersInternal[index] }
    override fun indexOf(element : DataColumnHeader) : Int = if (contains(element)) element.index else -1
    override fun isEmpty() : Boolean = size == 0
    override fun lastIndexOf(element : DataColumnHeader) : Int = indexOf(element)
    override fun containsKey(key : String) : Boolean = checkForSourceChanges { key in nameToIndexMap }
    override fun containsValue(value : DataColumnHeader) : Boolean = contains(value)
    override fun get(key : String) : DataColumnHeaderView? = checkForSourceChanges { nameToIndexMap[key]?.let { headersInternal[it] } }
    override fun listIterator() : ListIterator<DataColumnHeaderView> = listIterator(0)
    override fun listIterator(index : Int) : ListIterator<DataColumnHeaderView> = ensureIndexInRange(index, size, true) {
        object : ListIterator<DataColumnHeaderView> {
            private var view = concurrentModificationManager.View()
            private var cursor = index
            override fun hasNext() : Boolean = view.checkForChanges { cursor < size }
            override fun hasPrevious() : Boolean = view.checkForChanges { size != 0 && cursor >= 1 }
            override fun next() : DataColumnHeaderView = ensureHasNext { this@DataColumnHeadersView[cursor++] }
            override fun nextIndex() : Int = view.checkForAnyChanges { cursor }
            override fun previous() : DataColumnHeaderView = ensureHasPrevious { this@DataColumnHeadersView[--cursor] }
            override fun previousIndex() : Int = view.checkForAnyChanges { cursor - 1 }
        }
    }
    override fun subList(fromIndex : Int, toIndex : Int) : List<DataColumnHeaderView> = ensureSublistRange(fromIndex, toIndex, size) {
        object : List<DataColumnHeaderView> {
            private var view = concurrentModificationManager.View()
            override val size : Int get() = view.checkForAnyChanges { toIndex - fromIndex }
            override fun contains(element : DataColumnHeaderView) : Boolean = indexOf(element) != -1
            override fun containsAll(elements : Collection<DataColumnHeaderView>) : Boolean = if (elements.isEmpty()) view.checkForAnyChanges { true } else elements.all { contains(it) }
            override fun get(index : Int) : DataColumnHeaderView = view.checkForChanges { this@DataColumnHeadersView[index + fromIndex] }
            override fun indexOf(element : DataColumnHeaderView) : Int = view.checkForChanges { this@DataColumnHeadersView.indexOf(element).let { if (indexInRange(it, toIndex, fromIndex)) it else -1 } }
            override fun isEmpty() : Boolean = view.checkForAnyChanges { fromIndex == toIndex }
            override fun iterator() : Iterator<DataColumnHeaderView> = listIterator()
            override fun lastIndexOf(element : DataColumnHeaderView) : Int = indexOf(element)
            override fun listIterator() : ListIterator<DataColumnHeaderView> = listIterator(0)
            override fun listIterator(index : Int) : ListIterator<DataColumnHeaderView> = ensureIndexInRange(index, size, true) {
                object : ListIterator<DataColumnHeaderView> {
                    private var cursor = index
                    override fun hasNext() : Boolean = cursor < size
                    override fun hasPrevious() : Boolean = size != 0 && cursor != 0
                    override fun next() : DataColumnHeaderView = ensureHasNext { this@DataColumnHeadersView[cursor++ + fromIndex] }
                    override fun nextIndex() : Int = view.checkForAnyChanges { cursor }
                    override fun previous() : DataColumnHeaderView = ensureHasPrevious { this@DataColumnHeadersView[--cursor + fromIndex] }
                    override fun previousIndex() : Int = view.checkForAnyChanges { cursor - 1 }
                }
            }
            override fun subList(fromIndex : Int, toIndex : Int) : List<DataColumnHeaderView> = ensureSublistRange(fromIndex, toIndex, size) { this@DataColumnHeadersView.subList(fromIndex + fromIndex, toIndex + fromIndex) }
        }
    }
    override val keys : Set<String> get() = checkForSourceChanges {
        object : Set<String> {
            private val view = concurrentModificationManager.View()
            override val size : Int get() = view.checkForChanges { this@DataColumnHeadersView.size }
            override fun contains(element : String) : Boolean = view.checkForChanges { this@DataColumnHeadersView.containsKey(element) }
            override fun containsAll(elements : Collection<String>) : Boolean = if (elements.isEmpty()) view.checkForAnyChanges { true } else elements.all { contains(it) }
            override fun isEmpty() : Boolean = size == 0
            override fun iterator() : Iterator<String> = view.checkForChanges { this@DataColumnHeadersView.iterator().map { it.name } }
        }
    }
    override val values : Collection<DataColumnHeaderView> get() = checkForSourceChanges {
        object : Collection<DataColumnHeaderView> {
            private val view = concurrentModificationManager.View()
            override val size : Int get() = view.checkForChanges { this@DataColumnHeadersView.size }
            override fun contains(element : DataColumnHeaderView) : Boolean = view.checkForChanges { this@DataColumnHeadersView.contains(element) }
            override fun containsAll(elements : Collection<DataColumnHeaderView>) : Boolean = if (elements.isEmpty()) view.checkForAnyChanges { true } else elements.all { contains(it) }
            override fun isEmpty() : Boolean = size == 0
            override fun iterator() : Iterator<DataColumnHeaderView> = view.checkForChanges { this@DataColumnHeadersView.iterator().map { it } }
        }
    }
    override val entries : Set<Map.Entry<String, DataColumnHeaderView>> get() = checkForSourceChanges {
        object : Set<Map.Entry<String, DataColumnHeaderView>> {
            private val view = concurrentModificationManager.View()
            override val size : Int = view.checkForChanges { this@DataColumnHeadersView.size }
            override fun contains(element : Map.Entry<String, DataColumnHeaderView>) : Boolean = view.checkForChanges { this@DataColumnHeadersView.contains(element.value) && element.key == element.value.name }
            override fun containsAll(elements : Collection<Map.Entry<String, DataColumnHeaderView>>) : Boolean = if (elements.isEmpty()) view.checkForAnyChanges { true } else elements.all { contains(it) }
            override fun isEmpty() : Boolean = size == 0
            override fun iterator() : Iterator<Map.Entry<String, DataColumnHeaderView>> = view.checkForChanges {
                this@DataColumnHeadersView.iterator().map {
                    object : Map.Entry<String, DataColumnHeaderView> {
                        override val key : String = it.name
                        override val value : DataColumnHeaderView = it
                    }
                }
            }
        }
    }
}
