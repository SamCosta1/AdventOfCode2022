package y2022.day3

import puzzlerunners.Puzzle
import utils.RunMode
import java.nio.file.Files
import java.nio.file.Paths

class Main(
    override val part1ExpectedAnswerForSample: Any = 157,
    override val part2ExpectedAnswerForSample: Any = 70,
    override val isComplete: Boolean = true
): Puzzle {

    override fun runPart1(data: List<String>, runMode: RunMode) = data.map { raw ->
        val section1 = raw.take(raw.length / 2).toSet()
        val section2 = raw.takeLast(raw.length / 2).toSet()

        val shortest_longest = listOf(section1, section2).sortedBy { it.size }
        val shortest = shortest_longest.first()
        val longest = shortest_longest.last()

        val commonChars = shortest.filter { longest.contains(it) }
        commonChars.sumBy { it.priority() }
    }.sum()

    override fun runPart2(data: List<String>, runMode: RunMode) = data.chunked(3).map { list ->
        val commonChars = list.first().filter { list[1].contains(it) && list[2].contains(it) }.toSet()
        commonChars.sumBy { it.priority() }
    }.sum()

    fun Char.priority() = ("abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase()).indexOf(this) + 1
}