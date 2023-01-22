package y2021.day5

import utils.Point
import utils.Puzzle
import utils.RunMode
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sign

class Main : Puzzle {
    private fun String.parse() = split(" -> ").map { raw ->
        raw.split(",").map {
            it.toLong()
        }.let {
            Point(it.first(), it.last())
        }
    }.let { Pair(it.first(), it.last()) }

    private fun Pair<Point, Point>.isNotDiagonal() = first.x == second.x || first.y == second.y

    private fun Pair<Point, Point>.allPoints(): List<Point> {
        val points = mutableSetOf<Point>(first, second)

        var currentPoint = first

        while (currentPoint != second) {
            currentPoint = Point(
                currentPoint.x - (currentPoint.x - second.x).sign,
                currentPoint.y - (currentPoint.y - second.y).sign,
            )
            points.add(currentPoint)
        }

        return points.toList()
    }

    override fun runPart1(data: List<String>, runMode: RunMode) = countOverlaps(data.map { it.parse() }, false)

    private fun countOverlaps(lines: List<Pair<Point, Point>>, includeDiagonals: Boolean): Int {
        val points = mutableMapOf<Point, Int>()

        lines.filter {
            includeDiagonals || it.isNotDiagonal()
        }.forEach { startEnd ->
            startEnd.allPoints().forEach {
                points[it] = points.getOrDefault(it, 0) + 1
            }
        }

        return points.count { it.value >= 2 }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = countOverlaps(data.map { it.parse() }, true)
}
