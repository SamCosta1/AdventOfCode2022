package y2021.day9

import utils.Point
import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        grid.points.filter { entry ->
            grid.adjacentNodes(entry.key.x, entry.key.y).all { grid.points[it]!! > entry.value }
        }.values.sumBy { it + 1 }
    }

    data class Basin(val points: MutableSet<Point>)

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val pointsToBasin = mutableMapOf<Point, Basin>()

        grid.points.forEach { (point, value) ->
            if (value != 9 && !pointsToBasin.containsKey(point)) {
                pointsToBasin[point] = Basin(mutableSetOf()).also {
                    formBasin(it, grid, point)
                }
            }
        }

        pointsToBasin.values.toSet().sortedBy { it.points.size }.takeLast(3).let {
            it[0].points.size * it[1].points.size * it[2].points.size
        }
    }

    private fun formBasin(currentBasin: Basin, grid: Grid, currentPoint: Point) {
        grid.adjacentNodes(currentPoint.x, currentPoint.y).filter {
            grid.points[it] != 9 && !currentBasin.points.contains(it)
        }.forEach {
            currentBasin.points.add(it)
            formBasin(currentBasin, grid, it)
        }
    }
}
