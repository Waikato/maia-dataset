package māia.ml.dataset.type

import java.math.BigInteger

/**
 * Wrapper data-type which presents as an underlying data-type, but with the
 * added possibility that the value may be missing.
 *
 * @param base
 *          The base data-type.
 * @param internalConverter
 *          The missing values converter to handle the internal representation.
 * @param externalConverter
 *          The missing values converter to handle the external representation.
 * @param BI
 *          The internal type of values in the [base] data-type.
 * @param BX
 *          The external type of values in the [base] data-type.
 * @param B
 *          The type of the base data-type.
 * @param MI
 *          The type of internal values.
 * @param MX
 *          The type of external values.
 */
sealed class DataTypeWithMissingValues<BI, BX, out B : DataType<BI, BX>, MI, MX>(
    val base : B,
    val internalConverter : MissingValuesConverter<BI, MI>,
    val externalConverter : MissingValuesConverter<BX, MX>
) : DataType<MI, MX> {

    final override fun convertToExternal(value : MI) : MX {
        // If the internal value is missing, return an external missing value
        if (internalConverter.isMissing(value))
            return externalConverter.missing()

        return externalConverter.convertBaseToNotMissing(
                base.convertToExternal(
                        internalConverter.convertNotMissingToBase(value)
                )
        )
    }

    final override fun convertToInternal(value : MX) : MI {
        // If the external value is missing, return an internal missing value
        if (externalConverter.isMissing(value))
            return internalConverter.missing()

        return internalConverter.convertBaseToNotMissing(
                base.convertToInternal(
                        externalConverter.convertNotMissingToBase(value)
                )
        )
    }

    override fun isValidExternal(value : MX) : Boolean {
        return externalConverter.isMissing(value) ||
                base.isValidExternal(
                        externalConverter.convertNotMissingToBase(value)
                )
    }

    override fun isValidInternal(value : MI) : Boolean {
        return internalConverter.isMissing(value) ||
                base.isValidInternal(
                        internalConverter.convertNotMissingToBase(value)
                )
    }

    /**
     * Whether the given value represents a missing value of the internal type.
     *
     * @param value
     *          The value to check.
     * @return
     *          Whether the value is missing.
     */
    fun isMissingInternal(value: MI) : Boolean = internalConverter.isMissing(value)

    /**
     * Whether the given value represents a missing value of the external type.
     *
     * @param value
     *          The value to check.
     * @return
     *          Whether the value is missing.
     */
    fun isMissingExternal(value: MX) : Boolean = externalConverter.isMissing(value)

    /**
     * Gets a missing value of this data-type.
     */
    fun missing() : MX = externalConverter.missing()

    // Initial value for possibly-missing data-types is definitely-missing;
    // can be overridden though if a present value is more appropriate
    override fun initial() : MI = internalConverter.missing()

    final override fun equals(other : Any?) : Boolean {
        // Doesn't matter how missing values are represented, so long as the
        // underlying data-types match.
        return other is DataTypeWithMissingValues<*, *, *, *, *> && base == other.base
    }

    final override fun hashCode() : Int {
        return base.hashCode()
    }

    final override fun toString() : String {
        return "$base with missing values"
    }

}

/**
 * Wrapper data-type which presents as an underlying data-type, but with the
 * added possibility that the value may be missing.
 *
 * @param base
 *          The base data-type.
 * @param internalConverter
 *          The missing values converter to handle the internal representation.
 * @param externalConverter
 *          The missing values converter to handle the external representation.
 * @param BI
 *          The internal type of values in the [base] data-type.
 * @param BX
 *          The external type of values in the [base] data-type.
 * @param B
 *          The type of the base data-type.
 * @param MI
 *          The type of internal values.
 * @param MX
 *          The type of external values.
 */
class WithMissingValues<BI, BX, out B : DataType<BI, BX>, MI, MX>(
    base : B,
    internalConverter : MissingValuesConverter<BI, MI>,
    externalConverter : MissingValuesConverter<BX, MX>
) : DataTypeWithMissingValues<BI, BX, B, MI, MX>(
    base,
    internalConverter,
    externalConverter
) {
    constructor(
        base : DataTypeWithMissingValues<BI, BX, B, *, *>,
        internalConverter : MissingValuesConverter<BI, MI>,
        externalConverter : MissingValuesConverter<BX, MX>
    ): this(
        base.base,
        internalConverter,
        externalConverter
    )

    constructor(
        base : DataTypeWithMissingValues<BI, BX, B, MI, MX>
    ): this(
        base.base,
        base.internalConverter,
        base.externalConverter
    )
}

/**
 * Adds finite-entropy methods to data-types with missing values.
 *
 * @param base
 *          The base data-type.
 * @param internalConverter
 *          The missing values converter to handle the internal representation.
 * @param externalConverter
 *          The missing values converter to handle the external representation.
 * @param missingEntropy
 *          The amount of entropy to assign to the missing value. By default,
 *          missing values occupy zero entropy.
 * @param BI
 *          The internal type of values in the [base] data-type.
 * @param BX
 *          The external type of values in the [base] data-type.
 * @param B
 *          The type of the base data-type.
 * @param MI
 *          The type of internal values.
 * @param MX
 *          The type of external values.
 */
class WithFiniteMissingValues<BI, BX, out B : FiniteDataType<BI, BX>, MI, MX>(
    base : B,
    internalConverter : MissingValuesConverter<BI, MI>,
    externalConverter : MissingValuesConverter<BX, MX>,
    val missingEntropy : BigInteger = BigInteger.ZERO
) : DataTypeWithMissingValues<BI, BX, B, MI, MX>(
        base, internalConverter, externalConverter
), FiniteDataType<MI, MX> {

    constructor(
        base : DataTypeWithMissingValues<BI, BX, B, *, *>,
        internalConverter : MissingValuesConverter<BI, MI>,
        externalConverter : MissingValuesConverter<BX, MX>,
        missingEntropy : BigInteger = BigInteger.ZERO
    ): this(
        base.base,
        internalConverter,
        externalConverter,
        missingEntropy
    )

    constructor(
        base : DataTypeWithMissingValues<BI, BX, B, MI, MX>,
        missingEntropy : BigInteger = BigInteger.ZERO
    ): this(
        base.base,
        base.internalConverter,
        base.externalConverter,
        missingEntropy
    )

    init {
        // Make sure the provided null-entropy is non-negative
        if (missingEntropy < BigInteger.ZERO)
            throw IllegalArgumentException(
                    "Null-entropy for finite missing value types " +
                            "can't be negative (got $missingEntropy)")
    }

    override val entropy : BigInteger = base.entropy + missingEntropy

    override fun select(selection : BigInteger) : MX {
        if (selection >= base.entropy) return missing()
        return externalConverter.convertBaseToNotMissing(base.select(selection))
    }

}

/**
 * Converter from a definite-present type to a possibly-missing type.
 *
 * @param DP    The type of definitely-present values.
 * @param PM    The type of possibly-missing values.
 */
abstract class MissingValuesConverter<DP, PM> {

    /**
     * Whether a value in the possibly-missing type is missing.
     *
     * @param value     A value in the possibly-missing type.
     * @return          Whether the value is missing.
     */
    abstract fun isMissing(value : PM) : Boolean

    /**
     * Converts a not-missing value of the possibly-missing type
     * to the definitely-present type.
     *
     * @param value         The not-missing value.
     * @return              The same value in the definitely-present type.
     * @throws Exception    If [value] is missing.
     */
    abstract fun convertNotMissingToBase(value : PM) : DP

    /**
     * Converts a value of the definitely-present type
     * to a not-missing value of the definitely-present type.
     *
     * @param value         The definitely-present value.
     * @return              The same value in the possibly-missing type.
     */
    abstract fun convertBaseToNotMissing(value : DP) : PM

    /**
     * Returns a value of the possibly-missing type that represents
     * that the value is missing.
     *
     * @return  A canonical "missing" value.
     */
    abstract fun missing() : PM

}

/**
 * Missing-values converter which uses no value to represent a missing value.
 * Useful for representing types without missing values as types with missing
 * values.
 *
 * @param T
 *          Both the not-missing and missing value type.
 */
class NoMissingValuesConverter<T>: MissingValuesConverter<T, T>() {
    override fun isMissing(value : T) : Boolean = false
    override fun convertNotMissingToBase(value : T) : T  = value
    override fun convertBaseToNotMissing(value : T) : T = value
    override fun missing() : Nothing = throw Exception("No missing values")
}
