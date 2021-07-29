package māia.ml.dataset.util

/*
 * Performs type-unchecked conversion between internal and external
 * data-types.
 */

import māia.ml.dataset.type.DataType
import māia.ml.dataset.type.Nominal
import māia.ml.dataset.type.MissingValuesConverter
import māia.ml.dataset.type.DataTypeWithMissingValues

/**
 * Converts an external value to an internal value.
 *
 * @param value         The value to convert.
 * @throws Exception    If the value is invalid for this data-type.
 */
fun <I, X> DataType<I, X>.convertToInternalUnchecked(value : Any?) : I {
    return convertToInternal(value as X)
}

/**
 * Converts an internal value to an external value.
 *
 * @param value         The value to convert.
 * @throws Exception    If the value is invalid for this data-type.
 */
fun <I, X> DataType<I, X>.convertToExternalUnchecked(value : Any?) : X {
    return convertToExternal(value as I)
}

/**
 * TODO
 */
fun <I> DataType<I, *>.isValidInternalUnchecked(value : Any?) : Boolean {
    return isValidInternal(value as I)
}

/**
 * TODO
 */
fun <X> DataType<*, X>.isValidExternalUnchecked(value : Any?) : Boolean {
    return isValidExternal(value as X)
}

/**
 * TODO
 */
fun <MI> DataTypeWithMissingValues<*, *, *, MI, *>.isMissingInternalUnchecked(
    value : Any?
) : Boolean {
    return isMissingInternal(value as MI)
}

/**
 * TODO
 */
fun <MX> DataTypeWithMissingValues<*, *, *, *, MX>.isMissingExternalUnchecked(
    value : Any?
) : Boolean {
    return isMissingExternal(value as MX)
}

/**
 * TODO
 */
fun <PM> MissingValuesConverter<*, PM>.isMissingUnchecked(value : Any?) : Boolean {
    return isMissing(value as PM)
}

/**
 * TODO
 */
fun <DP, PM> MissingValuesConverter<DP, PM>.convertNotMissingToBaseUnchecked(value : Any?) : DP {
    return convertNotMissingToBase(value as PM)
}

/**
 * TODO
 */
fun <DP, PM> MissingValuesConverter<DP, PM>.convertBaseToNotMissingUnchecked(value : Any?) : PM {
    return convertBaseToNotMissing(value as DP)
}

/**
 * Gets the ordered index of the given category as represented internally.
 * Does not check that the category is of the valid internal type.
 *
 * @param category
 *          The category to get the index of.
 * @return
 *          The index of the category in the type's order.
 * @throws Exception
 *          If the category is not valid for this type.
 */
fun <I> Nominal<I>.indexOfInternalUnchecked(category: Any?): Int {
    return indexOfInternal(category as I)
}
