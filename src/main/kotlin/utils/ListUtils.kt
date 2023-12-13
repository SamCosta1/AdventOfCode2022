package utils

inline fun <T> Iterable<T>.productOf(selector: (T) -> Int): Int {
    var product: Int = 1.toInt()
    for (element in this) {
        product *= selector(element)
    }
    return product
}

fun <T> List<T>.safeSubList(fromIndex: Int, toIndex: Int): List<T> =
    if (fromIndex >= toIndex) emptyList() else this.subList(
        fromIndex.coerceAtLeast(0),
        toIndex.coerceAtLeast(0).coerceAtMost(this.size)
    )

inline fun <T> Iterable<T>.productOfLong(selector: (T) -> Long): Long {
    var product = 1L
    for (element in this) {
        product *= selector(element)
    }
    return product
}