package y2023.day21

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.Point
import utils.RunMode

typealias NumSteps = Long
class Main(
    override val part1ExpectedAnswerForSample: Any = 16,
    override val part2ExpectedAnswerForSample: Any = 16733044,
    override val isComplete: Boolean = false,
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (start, grid) ->

        val numSteps = when(runMode) {
            RunMode.Sample -> 6L
            RunMode.Real -> 64L
        }
        computePossibleEndSquares(grid, start, listOf(numSteps))[numSteps]!!
    }

    private fun computePossibleEndSquares(grid: GenericGrid<Parser.Plot>, start: Point, numSteps: List<NumSteps>): Map<NumSteps, Int> {
        val dist = mutableMapOf<Point, Long>()
        val sortedSteps = numSteps.sorted()
        dist[start] = 0L
        val q = mutableListOf(start)

        while (q.isNotEmpty()) {
            val u = q.minBy { dist[it]!! }
            q.remove(u)

            if (dist[u]!! > sortedSteps.last()) {
                return numSteps.associateWith { target ->
                    dist.count {
                        it.value == target || (it.value < target && (target - it.value) % 2L == 0L)
                    }
                }
            }

            listOf(u.top, u.left, u.right, u.bottom).filter {
                grid[it] == Parser.Plot.Garden
            }.forEach { v ->
                val alt = dist[u]!! + 1

                if (alt < dist.getOrDefault(v, Long.MAX_VALUE)) {
                    dist[v] = alt
                    q.add(v)
                }
            }
        }

        throw Exception("Shouldn't ever get here!")
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (start, grid) ->
        // Don't care enough to run for the sample
        if (runMode == RunMode.Sample) {
            return@let part2ExpectedAnswerForSample
        }
        val numSteps = when(runMode) {
            RunMode.Sample -> 5000L
            RunMode.Real -> 26501365L
        }

        val gridSize = data.size
        val remainder = numSteps % gridSize
        val divisor = numSteps / gridSize

        val referenceSteps = (0..<3).map { remainder + gridSize * it }
        val seq = computePossibleEndSquares(grid, start, referenceSteps).values.toIntArray()
        val multiplier = seq[2] - seq[1] - seq[1] + seq[0]

        // Looking for an equation of format an^2 + bn + c where n is the number of cycles + 1
        val a = multiplier / 2
        val b = seq[1] - seq[0] - 3*a
        val c = seq[0] - b - a

        val eqn =  { n: Long -> n*n*a + b*n + c }
        val seqNumber = divisor + 1
        eqn(seqNumber)
    }
}
