package y2022.day15

import kotlin.math.max
import kotlin.math.min

class Grid {
    enum class State {
        Sensor,
        Beacon,
        UnBeaconable,
        Empty;

        val char get() = when(this) {
            Sensor -> "S"
            Beacon -> "B"
            UnBeaconable -> "#"
            Empty -> "."
        }
    }

    val points = mutableMapOf<Point, State>()
    private var topLeftMostPoint = Point(Long.MAX_VALUE - 3, 0)
    private var bottomRightMostPoint = Point(Long.MIN_VALUE + 3, Long.MIN_VALUE + 3)

    operator fun get(point: Point) = points.getOrDefault(point, State.Empty)
    operator fun get(x: Long, y: Long) = points.getOrDefault(Point(x, y), State.Empty)
    operator fun set(point: Point, state: State) {
        points.put(point, state)

        topLeftMostPoint = Point(min(topLeftMostPoint.x, point.x), 0)
        bottomRightMostPoint =
            Point(max(bottomRightMostPoint.x, point.x), max(bottomRightMostPoint.y, point.y))
    }

    fun addAll(points: List<Point>, state: State) {
        points.forEach { this[it] = state  }
    }
}