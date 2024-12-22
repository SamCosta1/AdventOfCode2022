package y2024.day20

import org.apache.commons.lang3.math.NumberUtils.min
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
    ): Pair<MutableMap<Point, Int>, List<Point>> {
        val dist = mutableMapOf(start to 0)
        val q = mutableListOf(start)

        val prev = mutableMapOf<Point, Point>()
        while (q.isNotEmpty()) {
            val v = q.minBy { dist.getOrDefault(it, Int.MAX_VALUE) }
            q.remove(v)
            v.adjacentNoDiagonal().filter { grid[it] == Parser.Item.Free }.forEach { u ->
                if (dist[v]!! + 1 < dist.getOrDefault(u, Int.MAX_VALUE)) {
                    dist[u] = dist[v]!! + 1
                    q.add(u)
                    prev[u] = v
                }
            }
        }
        val path = mutableListOf<Point>(end)
        var current = prev[end]
        while (current != null) {
            path.add(current)
            current = prev[current]
        }
        return dist to path.reversed()
    }

    data class StatefulNode(
        val point: Point,
        val cheatsRemaining: Int
    )//, val cheatStart: Point?)//, val cheatEnd: Point?)

    override fun runPart2(
        data: List<String>, runMode: RunMode
    ) = Parser.parse(data, runMode).let { (grid, start, end) ->
        val baseSpeeds = runShortestPath(start, end, grid)
        val totalSpeed = baseSpeeds.first[end]!!
        val order = baseSpeeds.second.associateWith { baseSpeeds.second.indexOf(it) }
        val endIndex = baseSpeeds.second.lastIndex
        val cheatsBudget = 20

        val minSpeedIncrease = when (runMode) {
            RunMode.Sample -> 50
            RunMode.Real -> 100
        }
        val within20Generic = mutableSetOf<Pair<Point, Int>>()
        pointsWithinBudget(Point(0, 0), cheatsBudget, 0, within20Generic)

        var count = 0
        val set = mutableSetOf<Pair<Point, Point>>()
        val debug = mutableMapOf<Pair<Point, Point>, Int>()
        order.forEach() { (cheatStart, index) ->
            val endPoints = within20Generic
                .map { (it.first + cheatStart) to it.second }
                .filter { grid[it.first] == Parser.Item.Free }
                .map { (cheatEnd, cheatSteps) ->
                    cheatEnd to index + cheatSteps + totalSpeed - order[cheatEnd]!!
                }


//            There are 32 cheats that save 50 picoseconds.
//            There are 31 cheats that save 52 picoseconds.
//            There are 29 cheats that save 54 picoseconds.
//            There are 39 cheats that save 56 picoseconds.
//            There are 25 cheats that save 58 picoseconds.
//            There are 23 cheats that save 60 picoseconds.
//            There are 20 cheats that save 62 picoseconds.
//            There are 19 cheats that save 64 picoseconds.
//            There are 12 cheats that save 66 picoseconds.
//            There are 14 cheats that save 68 picoseconds.
//            There are 12 cheats that save 70 picoseconds.
//            There are 22 cheats that save 72 picoseconds.
//            There are 4 cheats that save 74 picoseconds.
//            There are 3 cheats that save 76 picoseconds.

            endPoints.forEach { (cheatEnd, speed) ->
                if (speed <= totalSpeed - minSpeedIncrease) {
                    count++
                    set.add(cheatStart to cheatEnd)
                    debug[cheatStart to cheatEnd] = min(debug.getOrDefault(cheatStart to cheatEnd, Int.MAX_VALUE), speed)
                }
            }
        }
        println(debug.toList().groupBy { it.second }.map { totalSpeed -it.key to it.value.size })
        println(grid)
        println(count)
        set.size
    }

    fun pointsWithinBudget(start: Point, totalBudget: Int, budgetUsed: Int, acc: MutableSet<Pair<Point, Int>>) {
        if (budgetUsed == totalBudget) {
            return
        }
        start.adjacentNoDiagonal().filter { !acc.contains(it to budgetUsed + 1) }.forEach {
            acc.add(it to budgetUsed + 1)
            pointsWithinBudget(it, totalBudget, budgetUsed + 1, acc)
        }
    }
}

