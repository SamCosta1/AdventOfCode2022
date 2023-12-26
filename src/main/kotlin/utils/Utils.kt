package utils

import puzzlerunners.DayResults
import puzzlerunners.ExecutionResult
import java.nio.file.Files
import java.nio.file.Paths
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
fun runTimedNew(repeats: Int = 1, block: () -> Any): ExecutionResult {
    var answer: Any? = null
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

 fun IntRange.coerceAtMost(value: Int) = (first..kotlin.math.min(last, value))
 fun IntRange.coerceAtLeast(value: Int) = (kotlin.math.max(first, value)..last)
 fun LongRange.coerceAtMost(value: Long) = (first..kotlin.math.min(last, value))
 fun LongRange.coerceAtLeast(value: Long) = (java.lang.Long.max(first, value)..last)
