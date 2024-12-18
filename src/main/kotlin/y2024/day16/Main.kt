package y2024.day16

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.MovementDirection
import utils.Point
import utils.RunMode
import kotlin.math.min

class Main(
    override val part1ExpectedAnswerForSample: Any = 11048,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val start = grid.points.filter { it.value == Parser.Item.Start }.keys.first()
        val end = grid.points.filter { it.value == Parser.Item.End }.keys.first()
        grid[start] = Parser.Item.Free
        grid[end] = Parser.Item.Free

        val direction = MovementDirection.East
        var minSoFar = Int.MAX_VALUE


        fun go(current: Point, direction: MovementDirection, scoreSoFar: Int, soFar: Set<Pair<Point, MovementDirection>>) {
            if (scoreSoFar >= minSoFar) {
                return
            }
            if (current == end) {
                println("Min So Far $minSoFar $runMode")
                minSoFar = scoreSoFar
                return
            }
            sequenceOf(
                direction,
                direction.turnRight90Degrees,
                direction.turnLeft90Degrees
            ).filter { grid[current(it)] == Parser.Item.Free}.forEach { newDir ->
                val nextPoint = current(newDir)
                if (!soFar.contains(nextPoint to newDir)) {
                    go(
                        nextPoint,
                        newDir,
                        scoreSoFar + if (newDir == direction) 1 else 1001,
                        soFar + (nextPoint to newDir)
                    )
                }
            }
        }

        go(current = start, direction, 0, setOf(start to direction))
        minSoFar
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
