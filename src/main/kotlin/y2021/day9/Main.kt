package y2021.day9

import utils.Point
import utils.Puzzle
import utils.RunMode

class Main : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        grid.points.filter { entry ->
            grid.adjacentNodes(entry.key.x, entry.key.y).all { grid.points[it]!! > entry.value }
        }.values.sumBy { it + 1 }
    }

    data class Basin(val points: MutableSet<Point>)

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val pointsToBasin = mutableMapOf<Point, Basin>()

        (0L until grid.height).forEach { y ->
            (0L until grid.width).forEach { x ->
                val point = Point(x, y)
                if (grid.points[point] != 9) {
                    val left = pointsToBasin[Point(x - 1, y)]
                    val above = pointsToBasin[Point(x, y-1)]

                    if (left != null && above != null && left != above) {
                        pointsToBasin[Point(x - 1, y)] = above
                        above.points.add(Point(x - 1, y))
                    }

                    val basin = above ?: left ?: Basin(mutableSetOf())
                    basin.points.add(point)
                    pointsToBasin[point] = basin
                }
            }
        }

        val basins = mutableSetOf<Basin>()
        pointsToBasin.keys.forEach {

        }

        pointsToBasin.values.toSet().sortedBy { it.points.size }.takeLast(3).let {
            it[0].points.size * it[1].points.size * it[2].points.size
        }
    }
}
