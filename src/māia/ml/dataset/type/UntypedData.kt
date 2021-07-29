package māia.ml.dataset.type

/**
 * Data-type which provides no static typing of its values. Both internal
 * and external values can be any type.
 *
 * @author Corey Sterling (csterlin at waikato dot ac dot nz)
 */
object UntypedData: DataType<Any?, Any?> {
    override fun initial() : Any? = null

    override fun convertToInternal(value : Any?) : Any? = value

    override fun convertToExternal(value : Any?) : Any? = value

    override fun isValidInternal(value : Any?) : Boolean = true

    override fun isValidExternal(value : Any?) : Boolean = true

    override fun equals(other : Any?) : Boolean = other is UntypedData

    override fun hashCode() : Int = super.hashCode()

    override fun toString() : String = "Untyped"
}
