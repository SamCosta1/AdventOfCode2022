package day14

import day14.Day14Main.*
import kotlin.math.max
import kotlin.math.min

class Grid {
    enum class State {
        Rock,
        Sand,
        Air;

        val char get() = when(this) {
            Rock -> "#"
            Sand -> "o"
            Air -> "."
        }
    }

    var hasFloor: Boolean = false

    private val points = mutableMapOf<Point, State>()
    private var topLeftMostPoint = Point(Int.MAX_VALUE - 3, 0)
    private var bottomRightMostPoint = Point(Int.MIN_VALUE + 3, Int.MIN_VALUE + 3)
    private var highestRockY = 0

    operator fun get(point: Point) = points.getOrDefault(point, State.Air)
    operator fun get(x: Int, y: Int) = points.getOrDefault(Point(x, y), State.Air)
    operator fun set(point: Point, state: State) {
        points.put(point, state)

        topLeftMostPoint = Point(min(topLeftMostPoint.x, point.x), 0)
        bottomRightMostPoint =
            Point(max(bottomRightMostPoint.x, point.x), max(bottomRightMostPoint.y, point.y))

        if (state == State.Rock) {
            highestRockY = max(highestRockY, point.y)
        }
    }

    fun isBlocked(point: Point) = (hasFloor && point.y == highestRockY + 2) || get(point) != State.Air

    fun isBelowBottomPoint(point: Point) = point.y > bottomRightMostPoint.y + 3

    override fun toString() = buildString {
        appendLine("${topLeftMostPoint.x}  ->  ${bottomRightMostPoint.x}")
        (topLeftMostPoint.y..bottomRightMostPoint.y + 5).forEach { y ->
            (topLeftMostPoint.x - 5..bottomRightMostPoint.x+5).forEach { x ->
                append(get(x,y).char)
            }
            appendLine()
        }
    }

    fun addAll(points: List<Point>, state: State) {
        points.forEach { this[it] = state  }
    }
}