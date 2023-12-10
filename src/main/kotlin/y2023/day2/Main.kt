package y2023.day2

import puzzlerunners.Puzzle
import utils.RunMode
import utils.productOf
import kotlin.math.max

class Main(
    override val part1ExpectedAnswerForSample: Any = 8,
    override val part2ExpectedAnswerForSample: Any = 2286,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { games ->
        val initialCounts = mapOf("red" to 12, "green" to 13, "blue" to 14)
        games.filter { isPossible(it, initialCounts) }.sumOf { it.id }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).sumOf { game ->
        val mins = mutableMapOf(
            "red" to Int.MIN_VALUE,
            "green" to Int.MIN_VALUE,
            "blue" to Int.MIN_VALUE
        )

        game.sets.flatten().forEach { entry ->
            mins[entry.color] = max(mins[entry.color]!!, entry.count)
        }

        mins.values.productOf { it }
    }

    private fun isPossible(game: Parser.Game, cubeCounts: Map<String, Int>) = game.sets.flatten().all { entry ->
        entry.count <= (cubeCounts[entry.color] ?: throw Exception("no such color"))
    }
}
