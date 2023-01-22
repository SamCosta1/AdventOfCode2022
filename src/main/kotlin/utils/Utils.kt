package utils

import java.nio.file.Files
import java.nio.file.Paths
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

fun Int.debug() = String.format("%02d", this)
fun Long.debug() = String.format("%02d", this)

fun parseFile(year: Int, day: Int, file: String) = Files.readAllLines(
    Paths.get(
        System.getProperty("user.dir"),
        "src/main/kotlin/y$year/day$day/$file"
    )
)