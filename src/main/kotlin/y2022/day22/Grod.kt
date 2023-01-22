package y2022.day22

import y2022.day15.Point
import y2022.day7.Day7Main.debug
import kotlin.math.max
import kotlin.math.min

/*
Top left is (1,1)
 */
class Grid( val width: Long,  val height: Long) {
    enum class State {
        Open,
        Rock,
        OffMap;

        val char
            get() = when (this) {
                OffMap -> " "
                Rock -> "#"
                Open -> "."
            }
        companion object {
            fun from(char: Char) = values().first { it.char[0] == char }
        }
    }

    data class CubeOverflow(val teleport: Point, val newDirection: Direction) {
        fun toPair() = Pair(teleport, newDirection)
    }
    data class RowColumnMetaData(val coord: Long, val firstRockOrOpen: Long, val lastRockOrOpen: Long, var firstTeleport: CubeOverflow? = null, var lastTeleport: CubeOverflow? = null)

    val columnMetaData = arrayOfNulls<RowColumnMetaData>(width.toInt() + 1) // ignoring index 0 for consistency
    val rowMetaData = arrayOfNulls<RowColumnMetaData>(height.toInt() + 1) // ignoring index 0 for consistency

    val points = mutableMapOf<Point, State>()
    val debugPoints = mutableMapOf<Point, Char>()

    operator fun get(point: Point) =
        points.getOrDefault(point, State.OffMap)

    operator fun get(x: Long, y: Long) = get(Point(x, y))

    operator fun set(point: Point, state: State) {
        points[point] = state

        if (state != State.OffMap) {
            val currentColumnMeta = columnMetaData[point.x.toInt()]
            columnMetaData[point.x.toInt()] = RowColumnMetaData(
                point.x,
                min(currentColumnMeta?.firstRockOrOpen ?: Long.MAX_VALUE - 10, point.y),
                max(currentColumnMeta?.lastRockOrOpen ?: Long.MIN_VALUE + 10, point.y)
            )

            val currentRowMeta = rowMetaData[point.y.toInt()]
            rowMetaData[point.y.toInt()] = RowColumnMetaData(
                point.y,
                min(currentRowMeta?.firstRockOrOpen ?: Long.MAX_VALUE - 10, point.x),
                max(currentRowMeta?.lastRockOrOpen ?: Long.MIN_VALUE + 10, point.x)
            )
        }
    }

    fun clear() {
        points.clear()
    }

    override fun toString() = buildString {
        appendLine("1  ->  $width")
        (1..height).forEach { y ->
            append(y.debug() + " ")
            (1..width).forEach { x ->
                append(debugPoints[Point(x, y)] ?: get(x, y).char)
            }
            appendLine()
        }
        appendLine()
    }

    fun addAll(points: List<Point>, state: State) {
        points.forEach { this[it] = state }
    }
}