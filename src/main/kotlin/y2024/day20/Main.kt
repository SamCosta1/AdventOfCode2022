package y2024.day20

import puzzlerunners.Puzzle
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
        val cheatsBudget = 2

        val minSpeedIncrease = when (runMode) {
            RunMode.Sample -> 2
            RunMode.Real -> 100
        }
        calculateNumberOfCheats(cheatsBudget, start, end, grid, minSpeedIncrease)
    }

    // this does not need djikstra but it was an easy copy and paste
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

    override fun runPart2(
        data: List<String>, runMode: RunMode
    ) = Parser.parse(data, runMode).let { (grid, start, end) ->
        val cheatsBudget = 20

        val minSpeedIncrease = when (runMode) {
            RunMode.Sample -> 50
            RunMode.Real -> 100
        }
        calculateNumberOfCheats(cheatsBudget, start, end, grid, minSpeedIncrease)
    }

    private fun calculateNumberOfCheats(
        cheatsBudget: Int,
        start: Point,
        end: Point,
        grid: GenericGrid<Parser.Item>,
        minSpeedIncrease: Int
    ): Int {
        val baseSpeeds = runShortestPath(start, end, grid)
        val totalSpeed = baseSpeeds.first[end]!!
        val order = baseSpeeds.second.associateWith { baseSpeeds.second.indexOf(it) }

        val withinBudgetGeneric = mutableSetOf<Pair<Point, Int>>()
        pointsWithinBudget(Point(0, 0), cheatsBudget, 0, withinBudgetGeneric)

        val set = mutableSetOf<Pair<Point, Point>>()
        order.forEach { (cheatStart, index) ->
            withinBudgetGeneric
                .asSequence()
                .map { (it.first + cheatStart) to it.second }
                .filter { grid[it.first] == Parser.Item.Free }
                .map { (cheatEnd, cheatSteps) ->
                    cheatEnd to index + cheatSteps + totalSpeed - order[cheatEnd]!!
                }.forEach { (cheatEnd, speed) ->
                    if (speed <= totalSpeed - minSpeedIncrease) {
                        set.add(cheatStart to cheatEnd)
                    }
                }
        }
        return set.size
    }

    private fun pointsWithinBudget(start: Point, totalBudget: Int, budgetUsed: Int, acc: MutableSet<Pair<Point, Int>>) {
        if (budgetUsed == totalBudget) {
            return
        }
        start.adjacentNoDiagonal().filter { !acc.contains(it to budgetUsed + 1) }.forEach {
            acc.add(it to budgetUsed + 1)
            pointsWithinBudget(it, totalBudget, budgetUsed + 1, acc)
        }
    }
}

