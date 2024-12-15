package y2024.day13

import puzzlerunners.NoSampleAnswer
import puzzlerunners.Puzzle
import utils.Point
import utils.RunMode
import utils.gaussianElimination
import kotlin.math.roundToLong

class Main(
    override val part1ExpectedAnswerForSample: Any = 480L,
    override val part2ExpectedAnswerForSample: Any = NoSampleAnswer,
    override val isComplete: Boolean = true
) : Puzzle {
    val aCost = 3
    val bCost = 1
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).asSequence().doTheThing()

    private fun Sequence<Parser.Machine>.doTheThing() = map {
        val matrix = it.matrix()
        it to gaussianElimination(matrix)
    }.map { (machine, doubles) ->
        (machine to doubles.map { double -> double.roundToLong() })
    }.filter { (machine, longs) ->
        machine.checkSolution(longs)
    }.sumOf { (_, sol) ->
        sol[0] * aCost + sol[1] * bCost
    }

    private fun Parser.Machine.checkSolution(ints: List<Long>): Boolean {
        val (a, b) = ints
        return a * xa + b * xb == prize.x && a * ya + b * yb == prize.y
    }

    override fun runPart2(data: List<String>, runMode: RunMode) =
        Parser.parse(data).asSequence().map {
            it.copy(prize = it.prize + Point(10000000000000, 10000000000000))
        }.doTheThing()

    private fun Parser.Machine.matrix() = listOf(
        listOf(xa, xb, prize.x).map { it.toDouble() }.toMutableList(),
        listOf(ya, yb, prize.y).map { it.toDouble() }.toMutableList()
    )
}