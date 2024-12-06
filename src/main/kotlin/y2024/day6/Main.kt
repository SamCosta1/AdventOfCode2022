package y2024.day6

import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.MovementDirection
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 41,
    override val part2ExpectedAnswerForSample: Any = 6,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (startPos, grid) ->
        var nextPos = startPos
        var direction = MovementDirection.North
        val points = mutableSetOf(startPos)
        var iterations = 0
        while (grid[nextPos] != Parser.Item.Outside) {
            if (grid[nextPos(direction)] == Parser.Item.Obstacle) {
                direction = direction.turnRight90Degrees
            } else {
                nextPos = nextPos(direction)
                points.add(nextPos)
            }
            iterations++
        }

        println("Iterations $iterations")
        points.forEach { grid[it] = Parser.Item.Outside }
        return@let points.size - 1
    }

    // 1988 1998 is too high
    fun GenericGrid<Parser.Item>.causesLoop(
        startPos: Point,
        startDirection: MovementDirection
    ): Boolean {
        var thisPos = startPos
        var direction = startDirection
        val newPointsAndDirections = mutableMapOf<Point, MutableSet<MovementDirection>>()
        while (this[thisPos] != Parser.Item.Outside) {
            if (this[thisPos(direction)] == Parser.Item.Obstacle) {
                direction = direction.turnRight90Degrees
                continue
            }

            if (this[thisPos] == Parser.Item.Outside) {
                return false
            } else if (
                    newPointsAndDirections[thisPos]?.contains(direction) == true
            ) {
                return true
            } else {
                newPointsAndDirections.getOrPut(thisPos) { mutableSetOf() }.add(direction)
                thisPos = thisPos(direction)
            }
        }

        return false
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (startPos, grid) ->
        var thisPos = startPos
        var direction = MovementDirection.North

        val solutions = mutableListOf<Pair<Point, MovementDirection>>()
        var iterations = 0
        while (grid[thisPos] != Parser.Item.Outside) {
            if (grid[thisPos(direction)] == Parser.Item.Obstacle) {
                direction = direction.turnRight90Degrees
                continue;
            }

            val nextPos = thisPos(direction)
            if (grid[nextPos] == Parser.Item.Outside) {
                break;
            }
            grid[nextPos] = Parser.Item.Obstacle

            if (
                grid.causesLoop(
                    thisPos,
                    direction.turnRight90Degrees,
                )
            ) {
                solutions.add(nextPos to direction.turnRight90Degrees)
            }
            grid[nextPos] = Parser.Item.Free

            thisPos = nextPos

            iterations++
//            if (iterations % 10 == 0) {
//            println("Iterated $iterations / 5705")
//            }
        }

        solutions.removeIf { it.first == startPos }
        solutions.forEach {
            grid[it.first] = Parser.Item.Outside
        }
//        println("solutions $solutions")
        println(grid)
        return@let solutions.map { it.first }.toSet().size
    }

}
