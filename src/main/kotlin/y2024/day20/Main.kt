package y2024.day20

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.GenericGrid
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 44,
    override val part2ExpectedAnswerForSample: Any = 285,
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
//        corrupted.forEach { (point, _) ->
//            grid[point] = Parser.Item.Free
//            val speed = runShortestPath(start, end, grid)
//            if (baseSpeed - speed >= minSpeedIncrease) {
//                count++
//            }
//            grid[point] = Parser.Item.Corrupted
//        }


        count
        part1ExpectedAnswerForSample
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

    data class StatefulNode(val point: Point, val cheatsRemaining: Int)

    override fun runPart2(
        data: List<String>, runMode: RunMode
    ) = Parser.parse(data, runMode).let { (grid, start, end) ->
        val baseSpeed = runShortestPath(start, end, grid)
        val cheatsBudget = 20

        val minSpeedIncrease = when (runMode) {
            RunMode.Sample -> 50
            RunMode.Real -> 100
        }

        val q = mutableListOf(StatefulNode(start, cheatsBudget))
        val dist = mutableMapOf(q.first() to 0)
        val prev = mutableMapOf<StatefulNode, MutableList<Pair<StatefulNode, String>>>()

        while (q.isNotEmpty()) {
            val v = q.minBy { dist.getOrDefault(it, Int.MAX_VALUE) }
            q.remove(v)

            v.point.adjacentNoDiagonal().forEach { u ->
                if (grid[u] == Parser.Item.Corrupted) {
                    if (v.cheatsRemaining > 1) {
                        val newCheats = v.cheatsRemaining - 2
                        val uNode = StatefulNode(u, newCheats)
                        if (dist[v]!! + 1 < dist.getOrDefault(uNode, Int.MAX_VALUE)) {
                            dist[uNode] = dist[v]!! + 1
                            prev[uNode] = mutableListOf(v to if (v.cheatsRemaining == cheatsBudget) "S" else if (newCheats == 0) "E" else "")
                            q.add(uNode)
                        }

                        if (dist[v]!! + 1 == dist.getOrDefault(uNode, Int.MAX_VALUE)) {
                            prev[uNode]!!.add(v to if (v.cheatsRemaining == cheatsBudget) "S" else if (newCheats == 0) "E" else "")
                        }
                    }
                } else if (grid[u] == Parser.Item.Free) {
                    val uNode = StatefulNode(
                        u,
                        if (grid[v.point] == Parser.Item.Corrupted) 0 else v.cheatsRemaining,
                    )
                    if (dist[v]!! + 1 < dist.getOrDefault(uNode, Int.MAX_VALUE)) {
                        dist[uNode] = dist[v]!! + 1
                        prev[uNode] = mutableListOf(v to if (grid[v.point] == Parser.Item.Corrupted) "E" else "")
                        q.add(uNode)
                    }

                    if (dist[v]!! + 1 == dist.getOrDefault(uNode, Int.MAX_VALUE)) {
                        prev[uNode]!!.add(v to if (grid[v.point] == Parser.Item.Corrupted) "E" else "")
                    }
                }
            }
        }
        println(dist.filter { it.key.point == end }.toList().joinToString("\n"))
        println("Prev")
        println(prev.filter { it.key.point == end }.toList().joinToString("\n"))
    }
}
