package y2021.day15

import utils.Point

class Grid(val width: Int, val height: Int) {
    val points = mutableMapOf<Point, Int>()

    override fun toString() = buildString {
        (0L until height).forEach { y ->
            (0L until width).forEach { x ->
                append(points[Point(x, y)]!!)
            }
            appendLine()

        }
    }

    fun adjacentNodes(x: Long, y: Long) = listOf(
        Point(x - 1L, y),
        Point(x, y + 1),
        Point(x + 1, y),
        Point(x, y - 1),
    ).filter { (it.x in 0 until width) && (it.y in 0 until height) }
}

object Parser {
    fun parse(raw: List<String>) = Grid(raw.first().length, raw.size).apply {
        raw.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                points[Point(x.toLong(), y.toLong())] = char.toString().toInt()
            }
        }
    }
}