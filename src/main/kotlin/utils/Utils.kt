package utils

import puzzlerunners.DayResults
import puzzlerunners.ExecutionResult
import java.math.BigDecimal
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.system.measureNanoTime
import kotlin.system.measureTimeMillis

enum class RunMode {
    Sample,
    Real
}

internal fun runTimed(repeats: Int = 1, block: () -> Any): String {
    var answer: Any? = null
    val time = measureTimeMillis {
        repeat(repeats) {
            answer = block()
        }
    }

    return "Average time ${String.format("%05d", time / repeats)}ms | $answer "
}

fun Long.formatTimeMs() = "${String.format("%05d", this)}ms"
fun <T> runTimedNew(repeats: Int = 1, block: () -> T): ExecutionResult<T> {
    var answer: T? = null
    val time = measureNanoTime {
        repeat(repeats) {
            answer = block()
        }
    }

    return ExecutionResult(answer, time)
}

fun Int.debug() = String.format("%02d", this)
fun Long.debug() = String.format("%02d", this)

fun parseFile(year: Int, day: Int, file: String) = Files.readAllLines(
    Paths.get(
        System.getProperty("user.dir"),
        "src/main/kotlin/y$year/day$day/$file"
    )
)

fun getAlphabetLetter(index: Int) = buildString {
    if (index < 0) {
        return "Invalid index"
    }
    if (index == 0) {
        return "A"
    }

    var remainingIndex = index
    while (remainingIndex > 0) {
        val charValue = (remainingIndex ) % 26
        insert(0, ('A' + charValue).toChar())
        remainingIndex /= 26
    }
}

fun rangeBetween(a: Int, b: Int) = min(a, b) .. max(a, b)
fun rangeBetween(a: Long, b: Long) = min(a, b) .. max(a, b)

 fun IntRange.coerceAtMost(value: Int) = (first..kotlin.math.min(last, value))
 fun IntRange.coerceAtLeast(value: Int) = (kotlin.math.max(first, value)..last)
 fun LongRange.coerceAtMost(value: Long) = (first..kotlin.math.min(last, value))
 fun LongRange.coerceAtLeast(value: Long) = (java.lang.Long.max(first, value)..last)

