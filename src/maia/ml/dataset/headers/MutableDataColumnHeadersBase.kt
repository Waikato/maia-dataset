package maia.ml.dataset.headers

import maia.ml.dataset.error.UnownedRepresentationError
import maia.ml.dataset.headers.header.DataColumnHeaderView
import maia.ml.dataset.headers.header.HeaderIdentityToken
import maia.ml.dataset.headers.header.MutableDataColumnHeader
import maia.ml.dataset.type.DataRepresentation
import maia.util.datastructure.ConcurrentModificationManager
import maia.util.ensureIndexInRange
import maia.util.error.UNREACHABLE_CODE

/**
 * TODO: What class does.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
sealed class MutableDataColumnHeadersBase:
    DataColumnHeadersBase(), DataColumnHeaders {

    abstract override val nameToIndexMap: MutableMap<String, Int>

    /** The names of the headers, in order. */
    abstract override val names: MutableList<String>

    /** Concurrent modification manager. */
    internal val concurrentModificationManager = ConcurrentModificationManager()

    override lateinit var identityToken : HeadersIdentityToken
        internal set

    /** Should be called when the structure is changed. */
    internal inline fun <R> changeStructure(
        crossinline block: () -> R
    ): R = concurrentModificationManager.performStructuralModification {
        block().also { identityToken = HeadersIdentityToken[this] }
    }

    /**
     * Checks if setting the column at the given [index] to the given [name]
     * would mean there are 2 columns with the same name.
     *
     * @param index
     *          The index of the column to change to [name].
     * @param name
     *          The new name for the column.
     * @throws IndexOutOfBoundsException
     *          If the index is not a valid column index.
     * @throws IllegalArgumentException
     *          If the name is already in use.
     */
    fun checkSet(index : Int, name: String) {
        // Can't set beyond the end of the list
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException("Index must be in [0, $size), got $index")
        }

        // Can't change a header's name to the same as another
        if (name in nameToIndexMap) {
            val inUseColumnIndex = nameToIndexMap[name]
            if (inUseColumnIndex != index) {
                throw IllegalArgumentException("Header name '$name' is already in use at column $inUseColumnIndex")
            }
        }
    }

    /**
     * Changes the name of a header.
     *
     * @param columnIndex
     *          The index that the header is in.
     * @param newName
     *          The new name the header is taking.
     */
    open fun changeName(
        columnIndex: Int,
        newName: String
    ) {
        checkSet(columnIndex, newName)
        val oldName = names[columnIndex]
        if (oldName == newName) return

        changeStructure {
            when (val header = this[columnIndex]) {
                is MutableDataColumnHeader -> header.name = newName
                is DataColumnHeaderView -> header.name = newName
            }
            nameToIndexMap.remove(oldName)
            nameToIndexMap[newName] = columnIndex
            names[columnIndex] = newName
        }
    }

    /**
     * Changes the isTarget status of a header.
     *
     * @param columnIndex
     *          The index that the header is in.
     * @param newIsTarget
     *          The new target status the header is taking.
     */
    open fun changeIsTarget(
        columnIndex: Int,
        newIsTarget: Boolean
    ) = ensureIndexInRange(columnIndex, size) {
        val header = this[columnIndex]
        val oldIsTarget = header.isTarget
        if (oldIsTarget == newIsTarget) return
        changeStructure {
            when (header) {
                is MutableDataColumnHeader -> header.isTarget = newIsTarget
                is DataColumnHeaderView -> header.isTarget = newIsTarget
                is HeaderIdentityToken -> UNREACHABLE_CODE(
                    "HeaderIdentityTokens are only held by HeadersIdentityTokens, " +
                            "which is not a sub-class of MutableDataColumnHeadersBase")
            }
        }
    }
}
