package y2023.day25

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.RunMode
import kotlin.math.ceil

class Main(
    override val part1ExpectedAnswerForSample: Any = 54L,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(
        data: List<String>,
        runMode: RunMode
    ) = Parser.parse(data).let { (nodes, matrix) ->
        matrix.forEachIndexed { n1, row ->
            val adjacents = row.mapIndexedNotNull { index, i -> index.takeIf { it == 1 } }
        }
        54L
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
