package utils

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

fun Puzzle.run(day: Int, year: Int) {
    val sample1 = readSample(1, day, year)
    val sample2 = readSample(2, day, year)
    val real = read("src/main/kotlin/y$year/day$day/data.txt")

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
                    runPart1(real, RunMode.Sample)
                }.let { "${it.first}\t(${it.second.formatTimeMs()})\"" }
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
                    runPart2(real, RunMode.Sample)
                }.let { "${it.first}\t(${it.second.formatTimeMs()})\"" }
            }")
        }
    }
}

private fun readSample(part: Int, day: Int, year: Int): List<String> {
    if (part == 1) return read("src/main/kotlin/y$year/day$day/sample.txt")

    return try {
        read("src/main/kotlin/y$year/day$day/sample-part2.txt")
    } catch (e: Exception) {
        read("src/main/kotlin/y$year/day$day/sample.txt")
    }!!
}
private fun runSample(time: Long, result: Any?, expectedResult: Any) = if (result == expectedResult) {
    Pair(true, " ✅: $result\t(${time.formatTimeMs()})")
} else {
    Pair(false, " ❌: $result\t(${time.formatTimeMs()}) [Expected Result = $expectedResult]")
}

private fun read(path: String) = Files.readAllLines(
    Paths.get(
        System.getProperty("user.dir"),
        path
    )
)