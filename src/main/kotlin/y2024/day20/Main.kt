package y2024.day20

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.GenericGrid
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 44,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(
        data: List<String>,
        runMode: RunMode
    ) = Parser.parse(data, runMode).let { (grid, start, end) ->
        val baseSpeed = runShortestPath(start, end, grid)

        val minSpeedIncrease = when (runMode) {
            RunMode.Sample -> 2
            RunMode.Real -> 100
        }
        var count = 0

        val corrupted = grid.points.filter { gridPoint ->
            gridPoint.value == Parser.Item.Corrupted
                    && !gridPoint.key.adjacentNoDiagonal().all { grid[it] == Parser.Item.Corrupted }
        }
        corrupted.forEach { (point, _) ->
            grid[point] = Parser.Item.Free
            val speed = runShortestPath(start, end, grid)
            if (baseSpeed - speed >= minSpeedIncrease) {
                count++
            }
            grid[point] = Parser.Item.Corrupted
        }

        count
    }

    private fun runShortestPath(
        start: Point,
        end: Point,
        grid: GenericGrid<Parser.Item>
    ): Int {
        val dist = mutableMapOf(start to 0)
        val q = mutableListOf(start)

        while (q.isNotEmpty()) {
            val v = q.minBy { dist.getOrDefault(it, Int.MAX_VALUE) }
            q.remove(v)
            v.adjacentNoDiagonal().filter { grid[it] == Parser.Item.Free }.forEach { u ->
                if (dist[v]!! + 1 < dist.getOrDefault(u, Int.MAX_VALUE)) {
                    dist[u] = dist[v]!! + 1
                    q.add(u)
                }
            }
        }
        return dist[end]!!
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
