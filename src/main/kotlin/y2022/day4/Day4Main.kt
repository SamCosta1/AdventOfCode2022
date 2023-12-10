package y2022.day4

import puzzlerunners.Puzzle
import utils.RunMode
import java.nio.file.Files
import java.nio.file.Paths

class Main(
    override val part1ExpectedAnswerForSample: Any = 0,
    override val part2ExpectedAnswerForSample: Any = 0,
    override val isComplete: Boolean = false
): Puzzle {

    override fun runPart1(data: List<String>, runMode: RunMode) = data.filter { raw ->
        val ranges = raw.split(",").map { range ->
            range.split("-").map { it.toInt() }
        }.sortedByDescending { it.last() }.sortedBy { it.first() }

        val rhsFirst = ranges.first().last()
        ranges.all { it.last() <= rhsFirst }
    }.count()

    override fun runPart2(data: List<String>, runMode: RunMode) = data.size - data.filter { raw ->
        val ranges = raw.split(",").map { range ->
            range.split("-").map { it.toInt() }
        }.sortedByDescending { it.last() }.sortedBy { it.first() }

        val rhsFirst = ranges.first().last()
        ranges.drop(1).all { rhsFirst < it.first() } // don't overlap
    }.count()
}