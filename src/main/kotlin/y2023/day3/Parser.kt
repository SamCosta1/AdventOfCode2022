package y2023.day3

import y2022.day15.Point
import y2022.day7.Day7Main.debug
import kotlin.math.max
import kotlin.math.min


object Parser {
    val numberRegex = "(\\d+)".toRegex()
    fun parse(data: List<String>): Grid = Grid().also { grid ->
        data.mapIndexed { rowIndex, row ->
            // Numbers
            numberRegex.findAll(row).forEach { matchResult ->
                val numberId = "($rowIndex,${matchResult.range.first})"
                matchResult.range.forEach {
                    grid.set(
                        x = it,
                        y = rowIndex,
                        Grid.State.Number(numberId, matchResult.groupValues.first().toLong())
                    )
                }
            }

            // Symbols
            row.forEachIndexed { index, char ->
                if (char.isSymbol) {
                    grid.set(x = index, y = rowIndex, Grid.State.Symbol(char))
                }
            }
        }
    }

    private val Char.isSymbol get() = toString().let { charString ->
        charString != "." && charString.toIntOrNull() == null
    }


    class Grid {
        sealed class State {

            val char
                get() = when (this) {
                    is Number -> "$value"
                    is Symbol -> "$value"
                    Dot -> "."
                }

            data class Number(val id: String, val value: Long) : State()
            data class Symbol(val value: Char) : State()
            object Dot : State()
        }

        val points = mutableMapOf<Point, State>()
        var topLeftMostPoint = Point(Long.MAX_VALUE - 3, 0)
        var bottomRightMostPoint = Point(Long.MIN_VALUE + 3, Long.MIN_VALUE + 3)

        operator fun get(point: Point) = points.getOrDefault(point, State.Dot)
        operator fun get(x: Long, y: Long) = points.getOrDefault(Point(x, y), State.Dot)
        operator fun set(x: Long, y: Long, state: State) = set(Point(x, y), state)
        operator fun set(x: Int, y: Int, state: State) = set(Point(x.toLong(), y.toLong()), state)
        operator fun set(point: Point, state: State) {
            points.put(point, state)

            if (state is State.Number) {
                topLeftMostPoint = Point(min(topLeftMostPoint.x, point.x), min(topLeftMostPoint.y, point.y))
                bottomRightMostPoint =
                    Point(max(bottomRightMostPoint.x, point.x), max(bottomRightMostPoint.y, point.y))
            }
        }

        override fun toString() = buildString {
            appendLine("${topLeftMostPoint.x}  ->  ${bottomRightMostPoint.x}")
            (topLeftMostPoint.y - 4..bottomRightMostPoint.y + 4).forEach { y ->
                append(y.debug() + " ")
                (topLeftMostPoint.x - 4..bottomRightMostPoint.x + 4).forEach { x ->
                    append(get(x, y).char)
                }
                appendLine()
            }
        }

        fun addAll(points: List<Point>, state: State) {
            points.forEach { this[it] = state }
        }
    }
}
