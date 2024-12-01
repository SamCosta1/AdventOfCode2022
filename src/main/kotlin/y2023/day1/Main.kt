package y2023.day1

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = NotStarted,
    override val part2ExpectedAnswerForSample: Any = 281,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = data.sumOf { row ->
        val first = row.first { it.toString().toIntOrNull() != null }.toString()
        val last = row.last { it.toString().toIntOrNull() != null }.toString()
        "$first$last".toInt()
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = data.map { row ->
        var newRow = row
        names.forEachIndexed { index, s ->
            newRow = newRow.replace(s, "${s.first()}${(index+1)}${s.last()}")
        }
        newRow
    }.let { runPart1(it, runMode) }

    private val names = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
}
