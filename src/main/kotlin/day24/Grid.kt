package day24

import day12.Day12Main
import day14.Day14Main.Point
import day7.Day7Main.debug

class Grid(val width: Int, val height: Int) {
    enum class Direction {
        Up,
        Down,
        Left,
        Right;

        val xDelta get() = when(this) {
            Up -> 0
            Down -> 0
            Left -> -1
            Right -> +1
        }
        val yDelta get() = when(this) {
            Up -> -1
            Down -> +1
            Left -> 0
            Right -> 0
        }

        val char get() = when(this) {
            Up -> '^'
            Down -> 'v'
            Left -> '<'
            Right -> '>'
        }
    }
    sealed class State {
        object Rock: State()
        object Entrance: State()
        object Exit: State()
        class Blizzard(val direction: Direction): State()

        val char get() = when(this) {
            is Blizzard -> direction.char
            Entrance -> "S"
            Exit -> "E"
            Rock -> "#"
        }
    }

    lateinit var exitPoint: Point
    lateinit var startPoint: Point
    val points = mutableMapOf<Point, MutableSet<State>>()
    val debugPoints = mutableMapOf<Point, Char>()

    operator fun get(x: Int, y: Int) = get(Point(x, y))
    operator fun get(point: Point) = points.getOrPut(point) { mutableSetOf() }

    fun add(x: Int, y: Int, state: State) {
        add(Point(x, y), state)
    }
    fun add(point: Point, state: State) {
        points.getOrPut(point) { mutableSetOf() }.add(state)

        if (state == State.Exit) {
            exitPoint = point
        }

        if (state == State.Entrance) {
            startPoint = point
        }
    }

    fun remove(x: Int, y: Int, state: State) {
        points[Point(x, y)]?.remove(state)
    }

    fun isRock(point: Point) = points[point]?.contains(State.Rock) == true

    fun copy() = Grid(width, height).also { newGrid ->
        newGrid.exitPoint = exitPoint
        newGrid.startPoint = startPoint
        points.forEach { (key, value) ->
            newGrid.points[key] = LinkedHashSet(value)
        }
    }
    override fun toString() = buildString {
        appendLine("0  ->  $width")
        (0 until height).forEach { y ->
            append(y.debug() + " ")
            (0 until width).forEach { x ->
                append(get(x, y).let {
                    if (it.size <= 1) {
                        it.firstOrNull()?.char ?: debugPoints.get(Point(x, y)) ?: "."
                    } else {
                        it.size
                    }
                })
            }
            appendLine()
        }
    }
}

fun Point.possibleMoves() = listOf(
    // Order important, intentionally doing the ones that go right and down first
    Point(x, y + 1),
    Point(x + 1, y),
    Point(x - 1, y),
    Point(x, y - 1),
)