package y2023.day24

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.Point3D
import utils.RunMode
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.min

class Main(
    override val part1ExpectedAnswerForSample: Any = 2L,
    override val part2ExpectedAnswerForSample: Any = 47,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { hailStones ->
        val testArea = when (runMode) {
            RunMode.Sample -> (BigDecimal(7)..BigDecimal(27.0))
            RunMode.Real -> (BigDecimal(200000000000000.0)..BigDecimal(400000000000000.0))
        }

        var intersections = 0
        forEachPair(hailStones) { stone1, stone2 ->
            val denominator = denominator(stone1.velocity, stone2.velocity)
            if (denominator != BigDecimal("0E-26")) {
                val alpha1 = BigDecimal(stone1.velocity.y).divide(BigDecimal(stone1.velocity.x), 26, RoundingMode.HALF_UP)
                val alpha2 = BigDecimal(stone2.velocity.y).divide(BigDecimal(stone2.velocity.x), 26, RoundingMode.HALF_UP)
                val numerator = BigDecimal(stone2.pos.y) - BigDecimal(stone1.pos.y) + alpha1 * BigDecimal(stone1.pos.x) - alpha2 * BigDecimal(stone2.pos.x)

                val xIntersection = numerator.divide(denominator, 26, RoundingMode.HALF_UP)
                val yIntersection = y(xIntersection, stone1.velocity, stone1.pos)

                val t = (yIntersection - BigDecimal(stone1.pos.y)).divide(BigDecimal(stone1.velocity.y), 26, RoundingMode.HALF_UP)
                val t2 = (yIntersection - BigDecimal(stone2.pos.y)).divide(BigDecimal(stone2.velocity.y), 26, RoundingMode.HALF_UP)

                if (t < BigDecimal.ZERO || t2 < BigDecimal.ZERO) {
                    // In the past
                } else {
                    if (testArea.contains(xIntersection) && testArea.contains(yIntersection)) {
                        intersections++
                    }
                }
            }
        }

        intersections / 2L
    }

    private fun denominator(v1: Point3D, v2: Point3D) =
        ((BigDecimal(v1.y) * BigDecimal(v2.x)) - (BigDecimal(v2.y) * BigDecimal(v1.x))).divide(BigDecimal(v1.x) * BigDecimal(v2.x), 26, RoundingMode.HALF_UP)

    private fun y(x: BigDecimal, velocity: Point3D, startPoint: Point3D): BigDecimal {
        return BigDecimal(startPoint.y) + ((BigDecimal(velocity.y).divide(BigDecimal(velocity.x), 26, RoundingMode.HALF_UP) * (x - BigDecimal(startPoint.x))))
    }

    private fun forEachPair(hailStones: List<Parser.HailStone>, block: (Parser.HailStone, Parser.HailStone) -> Unit) {
        hailStones.forEach { stone1 ->
            hailStones.forEach { stone2 ->
                if (stone1 != stone2) {
                    block(stone1, stone2)
                }
            }
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { hailStones ->

        if (runMode == RunMode.Sample) {
            return@let part2ExpectedAnswerForSample
        }

        val xMatches = hailStones.groupBy { it.velocity.x }.filter { it.value.size > 1 }.values
        val yMatches = hailStones.groupBy { it.velocity.y }.filter { it.value.size > 1 }.values
        val zMatches = hailStones.groupBy { it.velocity.z }.filter { it.value.size > 1 }.values

        var xPossibilities = setOf<Long>()

        val zeroDiff = xMatches.map { match ->
            (match.dropLast(1).mapIndexed { index, hailStone ->
                abs(hailStone.pos.x - match[index + 1].pos.x) to listOf(hailStone, match[index + 1])
            }.minBy { it.first })
        }.minBy { it.first }
        println(zeroDiff)

        yMatches.first().dropLast(1).forEachIndexed { index, hailStone ->
            println((hailStone.pos.y - yMatches.first()[index + 1].pos.y).factorsList())
        }
    }
}

fun Long.factorsList(): List<Long> =
(1..this/2).filter { this % it == 0L } + this
fun isPrime(number: Int): Boolean {
    if (number <= 1) {
        return false
    }

    for (i in 2 until number) {
        if (number % i == 0) {
            return false
        }
    }
    return true
}
