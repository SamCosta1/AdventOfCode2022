package utils

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
    val sample = read("src/main/kotlin/y$year/day$day/sample.txt")
    val real = read("src/main/kotlin/y$year/day$day/data.txt")

    if (part1ExpectedAnswerForSample != NotStarted) {
        println("Day $day")
        println("   Part 1")
        println(
            "       Sample${
                runTimedNew { runPart1(sample, RunMode.Sample) }.let {
                    runSample(it.second, it.first, part1ExpectedAnswerForSample)
                }
            }"
        )
        println("       Real  : ${
            runTimedNew {
                runPart1(real, RunMode.Sample)
            }.let { "${it.first} (${it.second.formatTimeMs()})\"" }
        }")
    }

    if (part2ExpectedAnswerForSample != NotStarted) {
        println("   Part 2")
        println(
            "       Sample${
                runTimedNew { runPart2(sample, RunMode.Sample) }.let {
                    runSample(it.second, it.first, part2ExpectedAnswerForSample)
                }
            }"
        )
        println("       Real  : ${
            runTimedNew {
                runPart2(real, RunMode.Sample)
            }.let { "${it.first} (${it.second.formatTimeMs()})\"" }
        }")
    }
}

private fun runSample(time: Long, result: Any?, expectedResult: Any) = if (result == expectedResult) {
    " ✅: $result (${time.formatTimeMs()})"
} else {
    " ❌: $result (${time.formatTimeMs()})"
}

private fun read(path: String) = Files.readAllLines(
    Paths.get(
        System.getProperty("user.dir"),
        path
    )
)