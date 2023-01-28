package y2021.day11

import utils.Point

class Grid(val width: Int, val height: Int) {
    val points = mutableMapOf<Point, Octopus>()

    override fun toString() = buildString {
        (0L until height).forEach { y ->
            (0L until width).forEach { x ->
                append(points[Point(x, y)]!!.energy)
            }
            appendLine()

        }
    }

    fun adjacentNodes(x: Long, y: Long) = listOf(
        Point(x - 1, y - 1),
        Point(x - 1L, y),
        Point(x - 1, y + 1),
        Point(x, y + 1),
        Point(x + 1, y + 1),
        Point(x + 1, y),
        Point(x + 1, y - 1),
        Point(x, y - 1),
    ).filter { (it.x in 0 until width) && (it.y in 0 until height) }
}

data class Octopus(val name: String, var energy: Int, var numFlashes: Long = 0) {
    override fun equals(other: Any?) = name == other
    override fun hashCode() = name.hashCode()
}

object Parser {
    fun parse(raw: List<String>) = Grid(10, 10).apply {
        (0L until 10).forEach { y ->
            (0L until 10).forEach { x ->
                points[Point(x, y)] = Octopus("$x,$y", raw[y.toInt()][x.toInt()].toString().toInt())
            }
        }
    }
}