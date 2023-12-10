package utils

import utils.Point
import y2022.day7.Day7Main.debug
import kotlin.math.max
import kotlin.math.min

class GenericGrid<Item : GenericGrid.GenericGridItem>(val defaultItem: Item) {

    interface GenericGridItem {
        val char: String
    }

    val points = mutableMapOf<Point, Item>()
    var topLeftMostPoint = Point(Long.MAX_VALUE - 3, 0)
    var bottomRightMostPoint = Point(Long.MIN_VALUE + 3, Long.MIN_VALUE + 3)

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
        appendLine("${topLeftMostPoint.x}  ->  ${bottomRightMostPoint.x}")
        (topLeftMostPoint.y - 1..bottomRightMostPoint.y + 1).forEach { y ->
            append(y.debug() + " ")
            (topLeftMostPoint.x - 1..bottomRightMostPoint.x + 1).forEach { x ->
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
    ).filter { (it.x in topLeftMostPoint.x .. bottomRightMostPoint.x) && (it.y in topLeftMostPoint.x .. bottomRightMostPoint.y) }

}