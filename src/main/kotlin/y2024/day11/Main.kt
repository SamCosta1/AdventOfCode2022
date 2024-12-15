package y2024.day11

import puzzlerunners.NoSampleAnswer
import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode
import y2022.day13.Parsing.parse

class Main(
    override val part1ExpectedAnswerForSample: Any = 55312L,
    override val part2ExpectedAnswerForSample: Any = NoSampleAnswer,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Paarser.parse(data).let { stones ->
        stones.sumOf { stone -> numberOfNewStonesAfterNSteps(stone, 25) + 1 }
    }

    private val cache = mutableMapOf<Pair<Long, Int>, Long>()
    private fun numberOfNewStonesAfterNSteps(startStone: Long, steps: Int): Long = cache.getOrPut(startStone to steps) {
        val nextStones = startStone.nextStones()

        if (steps == 1) {
            return@getOrPut nextStones.size - 1L
        }

        nextStones.sumOf { numberOfNewStonesAfterNSteps(it, steps - 1) } + (nextStones.size - 1)
    }

    private fun Long.nextStones() = when {
        this == 0L -> listOf(1L)
        this.toString().length % 2 == 0 -> this.toString()
            .let {
                listOf(
                    it.take(it.length / 2).toLong(),
                    it.takeLast(it.length / 2).toLong()
                )
            }

        else -> listOf(this * 2024L)
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Paarser.parse(data).let { stones ->
        stones.sumOf { stone -> numberOfNewStonesAfterNSteps(stone, 75) + 1 }
    }
}
