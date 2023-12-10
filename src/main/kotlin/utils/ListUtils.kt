package utils

inline fun <T> Iterable<T>.productOf(selector: (T) -> Int): Int {
    var product: Int = 1.toInt()
    for (element in this) {
        product *= selector(element)
    }
    return product
}

inline fun <T> Iterable<T>.productOfLong(selector: (T) -> Long): Long {
    var product = 1L
    for (element in this) {
        product *= selector(element)
    }
    return product
}