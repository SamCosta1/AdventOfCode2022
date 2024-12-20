package y2024.day18

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 22,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data, runMode).let { grid ->
        val start = Point(0,0)
        val end = grid.bottomRightMostPoint

        val dist = mutableMapOf(start to 0)
        val q = mutableListOf(start)

        while (q.isNotEmpty()) {
            val v = q.minBy { dist.getOrDefault(it, Int.MAX_VALUE) }
            q.remove(v)
            v.adjacentNoDiagonal().filter { grid[it] == Parser.Item.Free }.forEach { u ->
                if (dist[v]!! + 1 < dist.getOrDefault(u, Int.MAX_VALUE) ) {
                    dist[u] = dist[v]!! + 1
                    q.add(u)
                }
            }
        }
        dist[end]!!
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
