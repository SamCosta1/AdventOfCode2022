package puzzlerunners

import utils.RunMode
import utils.formatTimeMs
import utils.runTimedNew
import java.lang.Exception
import java.nio.file.Files
import java.nio.file.Paths

object NotStarted

interface Puzzle {
    val part1ExpectedAnswerForSample: Any
    val part2ExpectedAnswerForSample: Any
    fun runPart1(data: List<String>, runMode: RunMode): Any
    fun runPart2(data: List<String>, runMode: RunMode): Any
}

data class YearResults(
    val days: List<DayResults>,
    val runTime: Long,
    val year: Int,
)

data class DayResults(
    val part1Sample: ExecutionResult,
    val part1Real: ExecutionResult? = null,
    val part2Sample: ExecutionResult? = null,
    val part2Real: ExecutionResult? = null,
    val day: Int
) {
    val isComplete = part1Real != null && part2Sample != null && part2Real != null
}
data class ExecutionResult(val solution: Any?, val runtime: Long)

fun Puzzle.run(day: Int, year: Int): DayResults {
    val sample1 = readSample(1, day, year)
    val sample2 = readSample(2, day, year)
    val real = read("src/main/kotlin/y$year/day$day/data.txt")

    val part1Sample = runTimedNew { runPart1(sample1, runMode = RunMode.Sample) }
    val part1Real = if (part1Sample.solution == part1ExpectedAnswerForSample) runTimedNew {
        runPart1(real, runMode = RunMode.Real)
    } else {
        null
    }
    val part2Sample = if (part2ExpectedAnswerForSample != NotStarted) runTimedNew {
        runPart2(sample2, runMode = RunMode.Sample)
    } else {
        null
    }

    val part2Real = if (part2Sample?.solution == part2ExpectedAnswerForSample) runTimedNew {
        runPart2(real, runMode = RunMode.Real)
    } else {
        null
    }
    return DayResults(
        day = day,
        part1Sample = part1Sample,
        part1Real = part1Real,
        part2Sample = part2Sample,
        part2Real = part2Real
    )
}
/*
    if (part1ExpectedAnswerForSample != NotStarted) {
        var sampleCorrect = false
        println("Day $day")
        println("   Part 1")
        println(
            "       Sample${
                runTimedNew { runPart1(sample1, RunMode.Sample) }.let {
                    runSample(it.second, it.first, part1ExpectedAnswerForSample).let { result ->
                        sampleCorrect = result.first
                        result.second
                    }
                }
            }"
        )
        if (sampleCorrect) {
            println("       Real     : ${
                runTimedNew {
                    runPart1(real, RunMode.Real)
                }.let { "(${it.second.formatTimeMs()}) | ${it.first}" }
            }")
        }
    }

    if (part2ExpectedAnswerForSample != NotStarted) {
        var sampleCorrect = false
        println("   Part 2")
        println(
            "       Sample${
                runTimedNew { runPart2(sample2, RunMode.Sample) }.let {
                    runSample(it.second, it.first, part2ExpectedAnswerForSample).let { result ->
                        sampleCorrect = result.first
                        result.second
                    }
                }
            }"
        )
        if (sampleCorrect) {
            println("       Real     : ${
                runTimedNew {
                    runPart2(real, RunMode.Real)
                }.let { "(${it.second.formatTimeMs()}) | ${it.first}" }
            }")
        }
    }
}*/

private fun readSample(part: Int, day: Int, year: Int): List<String> {
    if (part == 1) return read("src/main/kotlin/y$year/day$day/sample.txt")

    return try {
        read("src/main/kotlin/y$year/day$day/sample-part2.txt")
    } catch (e: Exception) {
        read("src/main/kotlin/y$year/day$day/sample.txt")
    }!!
}
private fun runSample(time: Long, result: Any?, expectedResult: Any) = if (result == expectedResult) {
    Pair(true, " ✅: (${time.formatTimeMs()}) | $result")
} else {
    Pair(false, " ❌: (${time.formatTimeMs()}) | $result\t[Expected Result = $expectedResult]")
}

private fun read(path: String) = Files.readAllLines(
    Paths.get(
        System.getProperty("user.dir"),
        path
    )
)