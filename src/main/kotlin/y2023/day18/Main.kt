package y2023.day18

import puzzlerunners.Puzzle
import utils.MovementDirection
import utils.Point
import utils.RunMode
import kotlin.math.abs

class Main(
    override val part1ExpectedAnswerForSample: Any = 62L,
    override val part2ExpectedAnswerForSample: Any = 952408144115L,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = calculateArea(runMode, Parser.parse(data))

    private fun calculateArea(runMode: RunMode, input: List<Parser.InputLine>): Long {
        var currentPos = when (runMode) {
            RunMode.Sample -> Point(0, 0)
            RunMode.Real -> Point(188, 226)
        }

        val vertices = input.map { line ->
            val start = currentPos
            start(line.direction, line.amount).also { currentPos = it }
        }

        return picks(input.sumOf { it.amount } + 4, vertices.shoelace())
    }

    fun picks(pointsOnPerm: Long, innerPoints: Long) = innerPoints + (pointsOnPerm / 2) - 1
    private fun List<Point>.shoelace(): Long {
        var sum = 0L
        forEachIndexed { index, p1 ->
            val p2 = this[(index + 1) % size]

            sum += (p1.x * p2.y) - (p1.y * p2.x)
        }
        return abs(sum / 2)
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = calculateArea(runMode, Parser.parse(data).map {
        val amount = it.hash.dropLast(1).drop(1).toLong(radix = 16)
        val direction = when (it.hash.last()) {
            '0' -> MovementDirection.East
            '1' -> MovementDirection.South
            '2' -> MovementDirection.West
            '3' -> MovementDirection.North
            else -> throw Exception()
        }
        Parser.InputLine(direction, amount, "")
    })
}
