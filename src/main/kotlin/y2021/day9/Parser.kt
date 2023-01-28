package y2021.day9

import y2022.day12.Day12Main
import utils.Point
import y2022.day23.Grid

class Grid(val width: Int, val height: Int) {
    val points = mutableMapOf<Point, Int>()

    override fun toString() = buildString {
        (0L until height).forEach { y ->
            (0L until width).forEach { x ->
                append(points[Point(x, y)])
            }
            appendLine()

        }
    }

    fun adjacentNodes(x: Long, y: Long) = listOf(
//        Position(x - 1, y - 1),
        Point(x - 1L, y),
//        Position(x -1, y + 1),
        Point(x, y + 1),
//        Position(x + 1, y + 1),
        Point(x + 1, y),
//        Position(x + 1, y - 1),
        Point(x, y - 1),
    ).filter { (it.x in 0 until width) && (it.y in 0 until height)  }
}

object Parser {
    fun parse(raw: List<String>) = Grid(raw.first().length, raw.size).apply {
        raw.forEachIndexed { y, row ->
            row.forEachIndexed { x, value ->
                points[Point(x.toLong(), y.toLong())] = value.toString().toInt()
            }
        }
    }
}