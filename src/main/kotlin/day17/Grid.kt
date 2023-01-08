package day17

import day15.Point
import day7.Day7Main.debug
import kotlin.math.abs
import kotlin.math.max

enum class Rock {
    HLine,
    BackwardL,
    VLine,
    Plus,
    Square;

    companion object {
        val ordered
            get() = listOf(
                HLine,
                Plus,
                BackwardL,
                VLine,
                Square
            )
    }

    fun height() = abs(rockPoints(Point(0L, 0L)).minOf { it.y }) + 1 // + 1 To include the 0th row
    fun rockPoints(topLeft: Point) = when (this) {
        HLine -> (topLeft.x..topLeft.x + 3).map { Point(it, topLeft.y) }
        VLine -> (topLeft.y downTo topLeft.y - 3).map { Point(topLeft.x, it) }
        Plus -> (topLeft.x..topLeft.x + 2).map { Point(it, topLeft.y - 1) } + Point(topLeft.x + 1, topLeft.y) + Point(
            topLeft.x + 1,
            topLeft.y - 2
        )
        BackwardL -> (topLeft.y downTo topLeft.y - 2).map { Point(topLeft.x + 2, it) } + Point(
            topLeft.x,
            topLeft.y - 2
        ) + Point(topLeft.x + 1, topLeft.y - 2)
        Square -> (topLeft.x..topLeft.x + 1).map { x ->
            (topLeft.y downTo topLeft.y - 1).map { y ->
                Point(x, y)
            }
        }.flatten()
    }
}
// Grid where 0,0 is BOTTOM-LEFT of the cave

// x=0 is first column of space, width - 1 is last
// <=-1 & >=width +are cave wall

// y=0 is first row of available space
// y<=-1 is cave
class Grid(private val width: Int) {
    enum class State {
        Cave,
        Rock,
        Air;

        val char
            get() = when (this) {
                Cave -> "▓"
                Rock -> "#"
                Air -> "."
            }

        override fun toString() = when(this) {
            Cave,
            Rock -> super.toString()
            Air -> " " + super.toString()
        }
    }

    val points = mutableMapOf<Point, State>()
    private var topLeftMostPoint = Point(0, -1); private set
    private var bottomRightMostPoint = Point(width.toLong() - 1, 0)

    val topOfTowerYIndex get() = topLeftMostPoint.y
    operator fun get(point: Point) =
        points.getOrDefault(point, if (point.x < 0 || point.x >= width || point.y < 0) State.Cave else State.Air)

    operator fun get(x: Long, y: Long) = get(Point(x, y))

    operator fun set(point: Point, state: State) {
        points[point] = state

        topLeftMostPoint = Point(0, max(topLeftMostPoint.y, point.y))
    }

    fun clear() {
        points.clear()
        topLeftMostPoint = Point(0, -1)
    }
    override fun toString() = buildString {
        appendLine("${topLeftMostPoint.x}  ->  ${bottomRightMostPoint.x}")
        (topLeftMostPoint.y downTo bottomRightMostPoint.y - 1).forEach { y ->
            append(y.debug() + " ")
            (topLeftMostPoint.x - 2..bottomRightMostPoint.x + 2).forEach { x ->
                append(get(x, y).char)
            }
            appendLine()
        }

        append("     ")
        append("0123456")
        append("     ")
        appendLine()
    }

    fun addAll(points: List<Point>, state: State) {
        points.forEach { this[it] = state }
    }

    fun addRock(topLeft: Point, rock: Rock) = addAll(rock.rockPoints(topLeft), State.Rock)
    fun canMoveTo(topLeft: Point, rock: Rock) = rock.rockPoints(topLeft).all {
        get(it) == State.Air
    }
}

