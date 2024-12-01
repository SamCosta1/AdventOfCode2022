package y2024.day1

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode
import kotlin.math.abs

class Main(
    override val part1ExpectedAnswerForSample: Any = 11,
    override val part2ExpectedAnswerForSample: Any = 31,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any {
        val list1 = data.map { it.split("   ")[0] }.map { it.toInt() }.sorted()
        val list2 = data.map { it.split("   ")[1] }.map { it.toInt() }.sorted()

        return list1.mapIndexed() { index, i -> abs(i - list2[index]) }.sum()
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any {
        val list1 = data.map { it.split("   ")[0] }.map { it.toInt() }
        val list2 = data.map { it.split("   ")[1] }.map { it.toInt() }

        return list1.sumOf {  lst1Item ->
            lst1Item * list2.count { lst1Item == it }
        }
    }
}
