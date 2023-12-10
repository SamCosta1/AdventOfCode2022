package y2022.day1

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 24000,
    override val part2ExpectedAnswerForSample: Any = 45000,
    override val isComplete: Boolean = false
) : Puzzle {
    data class Elf(var calories: MutableList<Int>)

    override fun runPart1(data: List<String>, runMode: RunMode): Any = data.parse().maxOf { it.calories.sum() }

    override fun runPart2(data: List<String>, runMode: RunMode): Any =
        data.parse().sortedBy { it.calories.sum() }.takeLast(3).sumBy { it.calories.sum() }

    private fun List<String>.parse() = mutableListOf(Elf(mutableListOf())).also { result ->
        map { it.toIntOrNull() }.forEach {
            if (it == null) {
                result.add(Elf(mutableListOf()))
            } else {
                result.last().calories.add(it)
            }
        }
    }
}