package y2023.day21

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 16,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = false,
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (start, grid) ->
        val gardenPlots = grid.points.filter { it.value == Parser.Plot.Garden }
        val dist = gardenPlots.keys.associateWith { Int.MAX_VALUE }.toMutableMap()

        val numSteps = when(runMode) {
            RunMode.Sample -> 6
            RunMode.Real -> 64
        }
        dist[start] = 0
        val q = gardenPlots.keys.toMutableList()

        while (q.isNotEmpty()) {
            val u = q.minBy { dist[it]!! }
            q.remove(u)

            listOf(u.top, u.left, u.right, u.bottom).filter {
                grid[it] == Parser.Plot.Garden
            }.forEach { v ->
                val alt = dist[u]!! + 1

                if (alt < dist[v]!!) {
                    dist[v] = alt
                }
            }
        }

        dist.count {
            it.value == numSteps || (it.value < numSteps && (numSteps - it.value) % 2 == 0)
        }
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
