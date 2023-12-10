package y2023.day9

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 114L,
    override val part2ExpectedAnswerForSample: Any = 2L,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = data.map { row ->
        row.split(" ").map { it.toLong() }
    }.sumOf { list ->
        val differences = mutableListOf(list)

        while (differences.last().any { it != 0L }) {
            differences += differences.last().differences()
        }

        differences.sumOf { it.last() }
    }

    private fun List<Long>.differences() = drop(1).mapIndexed { index, number ->
        number - this[index]
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = data.map { row ->
        row.split(" ").map { it.toLong() }
    }.sumOf { list ->
        val differences = mutableListOf(list)

        while (differences.last().any { it != 0L }) {
            differences += differences.last().differences()
        }

        val firstElements = differences.map { it.first() }

        stupidPointlessRecursion(firstElements)
    }

    // It's 6am, my brain's not clever enough to do this iteratively
    private fun stupidPointlessRecursion(numbers: List<Long>): Long {
        if (numbers.size == 1) {
            return numbers.first()
        }

        return numbers.first() - stupidPointlessRecursion(numbers.drop(1))
    }
}
