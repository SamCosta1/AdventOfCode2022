package y2022.day23


import y2022.day15.Point
import y2022.day7.Day7Main.debug
import kotlin.math.max
import kotlin.math.min

class Grid {
    sealed class State {

        val char
            get() = when (this) {
                is Elf -> "#"
                Air -> "."
            }

        data class Elf(var currentPosition: Point) : State()
        object Air : State()
    }

    val points = mutableMapOf<Point, State>()
    var topLeftMostPoint = Point(Long.MAX_VALUE - 3, 0)
    var bottomRightMostPoint = Point(Long.MIN_VALUE + 3, Long.MIN_VALUE + 3)

    operator fun get(point: Point) = points.getOrDefault(point, State.Air)
    operator fun get(x: Long, y: Long) = points.getOrDefault(Point(x, y), State.Air)
    operator fun set(x: Long, y: Long, state: State) = set(Point(x, y), state)
    operator fun set(x: Int, y: Int, state: State) = set(Point(x.toLong(), y.toLong()), state)
    operator fun set(point: Point, state: State) {
        points.put(point, state)

        if (state is State.Elf) {
            topLeftMostPoint = Point(min(topLeftMostPoint.x, point.x), min(topLeftMostPoint.y, point.y))
            bottomRightMostPoint =
                Point(max(bottomRightMostPoint.x, point.x), max(bottomRightMostPoint.y, point.y))
        }
    }

    override fun toString() = buildString {
        appendLine("${topLeftMostPoint.x}  ->  ${bottomRightMostPoint.x}")
        (topLeftMostPoint.y - 4..bottomRightMostPoint.y +4).forEach { y ->
            append(y.debug() + " ")
            (topLeftMostPoint.x-4..bottomRightMostPoint.x+4).forEach { x ->
                append(get(x, y).char)
            }
            appendLine()
        }
    }

    fun addAll(points: List<Point>, state: State) {
        points.forEach { this[it] = state }
    }
}