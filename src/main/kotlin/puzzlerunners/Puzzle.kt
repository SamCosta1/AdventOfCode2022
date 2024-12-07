package puzzlerunners

import utils.RunMode
import utils.formatTimeMs
import utils.runTimedNew
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths
import java.text.DecimalFormat
import java.util.logging.Logger
import kotlin.math.round

object NotStarted

interface Puzzle {
    val part1ExpectedAnswerForSample: Any
    val part2ExpectedAnswerForSample: Any
    val isComplete: Boolean
    fun runPart1(data: List<String>, runMode: RunMode): Any
    fun runPart2(data: List<String>, runMode: RunMode): Any
}

data class YearResults(
    val days: List<DayResults>,
    val runTime: Long,
    val year: Int,
)

data class DayResults(
    val part1Sample: ExecutionResult<Any>,
    val part1Real: ExecutionResult<Any>? = null,
    val part2Sample: ExecutionResult<Any>? = null,
    val part2Real: ExecutionResult<Any>? = null,
    val day: Int,
    val year: Int,
    val puzzle: Puzzle
) {
    val isComplete = part1Real != null && part2Sample != null && part2Real != null
    val totalRuntime get() = listOfNotNull(
        part1Sample,part1Real,part2Sample,part2Real
    ).sumOf { it.runtime }
}
data class ExecutionResult<T>(val solution: T?, val runtime: Long) {
    fun formatRuntime(): String = (decimalFormatter.format(runtime / 1000_000.0) + "ms ${
        if (runtime > 1000) "(" + decimalFormatter.format(round(runtime / 1000_000_000.0)) + "s)" else ""
    }").takeUnless { solution == Unit } ?: "DNF"
    companion object {
        val decimalFormatter = DecimalFormat("#.###");
    }
}

fun Puzzle.run(day: Int, year: Int): DayResults {
    val sample1 = readSample(1, day, year)
    val sample2 = readSample(2, day, year)
    val real = read("src/main/kotlin/y$year/day$day/data.txt")

    val part1Sample = runTimedNew { runPart1(sample1, runMode = RunMode.Sample) }
    val part1Real = if (part1Sample.solution == part1ExpectedAnswerForSample) runTimedNew {
        runCatchingAndLog { runPart1(real, runMode = RunMode.Real) }
    } else {
        null
    }
    val part2Sample = if (part2ExpectedAnswerForSample != NotStarted) runTimedNew {
        runCatchingAndLog { runPart2(sample2, runMode = RunMode.Sample) }
    } else {
        null
    }

    val part2Real = if (part2Sample?.solution == part2ExpectedAnswerForSample) runTimedNew {
        runCatchingAndLog { runPart2(real, runMode = RunMode.Real) }
    } else {
        null
    }
    return DayResults(
        day = day,
        puzzle = this,
        part1Sample = part1Sample,
        part1Real = part1Real,
        part2Sample = part2Sample,
        part2Real = part2Real,
        year = year
    )
}

fun runCatchingAndLog(block: () -> Any) = runCatching {
    block()
}.let {
    it.exceptionOrNull()?.printStackTrace()
    it.getOrNull()
} ?: "<Execution Failed>"

private fun readSample(part: Int, day: Int, year: Int): List<String> {
    if (part == 1) return read("src/main/kotlin/y$year/day$day/sample.txt")

    return try {
        read("src/main/kotlin/y$year/day$day/sample-part2.txt")
    } catch (e: Exception) {
        read("src/main/kotlin/y$year/day$day/sample.txt")
    }!!
}

private fun read(path: String) = Files.readAllLines(
    Paths.get(
        System.getProperty("user.dir"),
        path
    )
)