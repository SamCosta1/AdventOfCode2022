package y2024.day18

import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 22,
    override val part2ExpectedAnswerForSample: Any = "6,1",
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parsePart1(data, runMode).let { grid ->
        val (end, dist) = runShortestPath(grid)
        dist[end]!!
    }

    private fun runShortestPath(grid: GenericGrid<Parser.Item>): Pair<Point, Map<Point, Int>> {
        val start = Point(0, 0)
        val end = grid.bottomRightMostPoint

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
        return Pair(end, dist)
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data, runMode).let { (bytes, grid) ->
        bytes.forEach { byte ->
            grid[byte] = Parser.Item.Corrupted
            val (end, dist) = runShortestPath(grid)
            if (byte == Point(6, 1)) {
                println(grid)
            }
            if (dist[end] == null) {
                return@let "${byte.x},${byte.y}"
            }
        }

        throw Exception("Didn't find a solution")
    }
}
