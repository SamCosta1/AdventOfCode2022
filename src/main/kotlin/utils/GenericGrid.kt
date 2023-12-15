package utils

import y2023.day14.Parser
import kotlin.math.max
import kotlin.math.min

class GenericGrid<Item : GenericGrid.GenericGridItem>(val defaultItem: Item) {
    interface GenericGridItem {
        val char: String
    }

    val points = mutableMapOf<Point, Item>()
    var topLeftMostPoint = Point(Long.MAX_VALUE - 3, 0)
    var bottomRightMostPoint = Point(Long.MIN_VALUE + 3, Long.MIN_VALUE + 3)
    var printBuffer = 0

    operator fun get(point: Point) = points.getOrDefault(point, defaultItem)
    operator fun get(x: Long, y: Long) = points.getOrDefault(Point(x, y), defaultItem)
    operator fun set(x: Long, y: Long, item: Item) = set(Point(x, y), item)
    operator fun set(x: Int, y: Int, item: Item) = set(Point(x.toLong(), y.toLong()), item)
    operator fun set(point: Point, item: Item) {
        points.put(point, item)

        topLeftMostPoint = Point(min(topLeftMostPoint.x, point.x), min(topLeftMostPoint.y, point.y))
        bottomRightMostPoint =
            Point(max(bottomRightMostPoint.x, point.x), max(bottomRightMostPoint.y, point.y))

    }

    override fun toString() = buildString {
        val maxXIndexDigits = max(
            (topLeftMostPoint.x - printBuffer).toString().length,
            (bottomRightMostPoint.x - printBuffer).toString().length,
        )
        val maxYDigits = max(
            (bottomRightMostPoint.y + printBuffer).toString().length,
            (topLeftMostPoint.y - printBuffer).toString().length,
        )

        repeat(maxXIndexDigits) { digitIndex ->
            append((1..maxYDigits).joinToString("") { " " })
            append("│")

            (topLeftMostPoint.x - printBuffer..bottomRightMostPoint.x + printBuffer).forEach {
                append(String.format("%0${maxXIndexDigits}d", it)[digitIndex])
            }
            appendLine()
        }
        (topLeftMostPoint.y - printBuffer..bottomRightMostPoint.y + printBuffer).forEach { y ->
            append(String.format("%0${maxYDigits}d", y) + "│")
            (topLeftMostPoint.x - printBuffer..bottomRightMostPoint.x + printBuffer).forEach { x ->
                append(get(x, y).char)
            }
            appendLine()
        }
    }

    fun addAll(points: List<Point>, item: Item) {
        points.forEach { this[it] = item }
    }

    fun adjacentPoints(point: Point) = adjacentPoints(point.x, point.y)
    fun adjacentPoints(x: Long, y: Long) = listOf(
        Point(x - 1, y - 1),
        Point(x - 1L, y),
        Point(x - 1, y + 1),
        Point(x, y + 1),
        Point(x + 1, y + 1),
        Point(x + 1, y),
        Point(x + 1, y - 1),
        Point(x, y - 1),
    ).filter { (it.x in topLeftMostPoint.x..bottomRightMostPoint.x) && (it.y in topLeftMostPoint.x..bottomRightMostPoint.y) }

}