package y2023.day24

import puzzlerunners.Puzzle
import utils.Point3D
import utils.RunMode
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.roundToLong
import kotlin.math.sqrt

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
                val alpha1 =
                    BigDecimal(stone1.velocity.y).divide(BigDecimal(stone1.velocity.x), 26, RoundingMode.HALF_UP)
                val alpha2 =
                    BigDecimal(stone2.velocity.y).divide(BigDecimal(stone2.velocity.x), 26, RoundingMode.HALF_UP)
                val numerator =
                    BigDecimal(stone2.pos.y) - BigDecimal(stone1.pos.y) + alpha1 * BigDecimal(stone1.pos.x) - alpha2 * BigDecimal(
                        stone2.pos.x
                    )

                val xIntersection = numerator.divide(denominator, 26, RoundingMode.HALF_UP)
                val yIntersection = y(xIntersection, stone1.velocity, stone1.pos)

                val t = (yIntersection - BigDecimal(stone1.pos.y)).divide(
                    BigDecimal(stone1.velocity.y),
                    26,
                    RoundingMode.HALF_UP
                )
                val t2 = (yIntersection - BigDecimal(stone2.pos.y)).divide(
                    BigDecimal(stone2.velocity.y),
                    26,
                    RoundingMode.HALF_UP
                )

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
        ((BigDecimal(v1.y) * BigDecimal(v2.x)) - (BigDecimal(v2.y) * BigDecimal(v1.x))).divide(
            BigDecimal(v1.x) * BigDecimal(
                v2.x
            ), 26, RoundingMode.HALF_UP
        )

    private fun y(x: BigDecimal, velocity: Point3D, startPoint: Point3D): BigDecimal {
        return BigDecimal(startPoint.y) + ((BigDecimal(velocity.y).divide(
            BigDecimal(velocity.x),
            26,
            RoundingMode.HALF_UP
        ) * (x - BigDecimal(startPoint.x))))
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
        fun gaussianElimination(coefficients: List<MutableList<Double>>): List<Double> {
            val rows = coefficients.size
            val cols = coefficients.first().size

            // This only works on a square matrix (with one extra column for the
            // coefficient on the right-hand side of the equation).
            require(rows == cols - 1) {
                throw Exception(
                    "The number of coefficients on the left side of the" +
                            "equation should be equal to the number of equations."
                )
            }

            // We operate on each row in the matrix of coefficients.
            for (row in coefficients.indices) {

                // Normalize the row starting with the diagonal value of each row.
                val pivot = coefficients[row][row]
                for (col in coefficients[row].indices) {
                    coefficients[row][col] /= pivot
                }

                // Sweep the other rows with `row`
                for (otherRow in coefficients.indices) {
                    if (row == otherRow) continue

                    val factor = coefficients[otherRow][row]
                    for (col in coefficients[otherRow].indices) {
                        coefficients[otherRow][col] -= factor * coefficients[row][col]
                    }
                }
            }

            return coefficients.map { it.last() }.toList()
        }

                val (rockX, rockY, rockDX, rockDY) = gaussianElimination(
                    hailStones.take(5).windowed(2).map { (h1, h2) ->
                        // This comes from:
                        // (dy'-dy)xR + (dx - dx')yR + (y - y')dxR + (x' - x)dyR = y*dx - x*dy -y'*dx' + x'dy'
                        mutableListOf(
                            h2.velocity.y - h1.velocity.y,  // (dy' - dy)
                            h1.velocity.x - h2.velocity.x,  // (dx - dx')
                            h1.pos.y - h2.pos.y,    // (y - y')
                            h2.pos.x - h1.pos.x,    // (x' - x')

                            // This is the right-hand side of the equation, or
                            // y*dx - x*dy -y'*dx' + x'dy'
                            ((h1.pos.y * h1.velocity.x) + (-h1.pos.x * h1.velocity.y) + (-h2.pos.y * h2.velocity.x) + (h2.pos.x * h2.velocity.y))
                        ).map { it.toDouble() }.toMutableList()
                    }).map { it.roundToLong() } // Prevents issues with precision

                val (rockZ, rockDZ) = gaussianElimination(
                    hailStones.take(3).windowed(2).map { (h1, h2) ->
                        mutableListOf(
                            // This comes from:
                            // (dx - dx')zR + (x' - x)dzR = z*dx - x*dz -z'*dx' + x'dz' - (dz'-dz)xR - (z - z')dxR
                            h1.velocity.x - h2.velocity.x,  // (dx - dx')
                            h2.pos.x - h1.pos.x,    // (x' - x)

                            // This is the right-hand side again
                            //  z*dx - x*dz - z'*dx' + x'dz' - (dz'-dz)xR - (z - z')dxR
                            // @formatter:off
                            ( (h1.pos.z  * h1.velocity.x)                       // z*dx
                                    - (h1.pos.x  * h1.velocity.z)                       // x*dz
                                    - (h2.pos.z  * h2.velocity.x)                       // z'*dx'
                                    + (h2.pos.x  * h2.velocity.z)                       // x'*dz'
                                    - ((h2.velocity.z - h1.velocity.z) * rockX.toDouble())  // (dz'-dz)xR
                                    - ((h1.pos.z  - h2.pos.z)  * rockDX))           // (z - z')dxR
                            //@formatter:on
                        ).map { it.toDouble() }.toMutableList()
                    }).map { it.roundToLong() }

                // We've solved for all the necessary parameters now, return the sum!
                return rockX + rockY + rockZ
            }
}
