package y2021.day7

import utils.Puzzle
import utils.RunMode
import kotlin.math.abs
import kotlin.math.min

class Main : Puzzle {


    override fun runPart1(data: List<String>, runMode: RunMode) =
        data.first().split(",").map { it.toLong() }.let { crabPositions ->
            var bestSoFar = Long.MAX_VALUE - 1

            (0..crabPositions.max()!!).forEach { aligmentPosition ->
                var current = 0L
                for (it in crabPositions) {
                    current += abs(it - aligmentPosition)
                    if (current > bestSoFar) {
                        continue
                    }
                }

                bestSoFar = min(bestSoFar, current)
            }

            bestSoFar
        }

    override fun runPart2(data: List<String>, runMode: RunMode) =
        data.first().split(",").map { it.toLong() }.let { crabPositions ->
            var bestSoFar = Long.MAX_VALUE - 1

            (0..crabPositions.max()!!).forEach { aligmentPosition ->
                var current = 0L
                for (it in crabPositions) {
                    current += sumOfFirstN(abs(it - aligmentPosition))
                    if (current > bestSoFar) {
                        continue
                    }
                }

                bestSoFar = min(bestSoFar, current)
            }

            bestSoFar
        }

    private fun sumOfFirstN(n: Long) = (n * (n+1)) / 2

}
