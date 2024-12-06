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
    // wrong 1838

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (startPos, grid) ->
        var currentPos = startPos(MovementDirection.North)
        var currentDirection = MovementDirection.North

        val sols = mutableSetOf<Point>()
        while (grid[currentPos] != Parser.Item.Outside) {
            val forwardOne = currentPos(currentDirection)
            if (grid[forwardOne] == Parser.Item.Obstacle) {
                currentDirection = currentDirection.turnRight90Degrees
                continue
            }

            if (grid[forwardOne] == Parser.Item.Outside) {
                println("MAIN: we're done $forwardOne")
                break
            }

            grid[forwardOne] = Parser.Item.Obstacle

            val visited = mutableMapOf<Point, MutableSet<MovementDirection>>()
            var currentLoopPos = startPos
            var currentLoopDirection = MovementDirection.North
            while (grid[currentLoopPos] != Parser.Item.Outside) {
                val forwardOneLoop = currentLoopPos(currentLoopDirection)
                if (grid[forwardOneLoop] == Parser.Item.Obstacle) {
                    currentLoopDirection = currentLoopDirection.turnRight90Degrees
                    continue
                }

                if (grid[forwardOneLoop] == Parser.Item.Outside) {
                    break
                }

                if (grid[forwardOneLoop] == Parser.Item.Free) {
                    if (visited[currentLoopPos]?.contains(currentLoopDirection) == true) {
                        sols.add(forwardOne)
                        break
                    } else {
                        visited.getOrPut(currentLoopPos) { mutableSetOf<MovementDirection>() }.add(currentLoopDirection)
                    }
                }
                currentLoopPos = forwardOneLoop
            }

            grid[forwardOne] = Parser.Item.Free
            currentPos = forwardOne
        }

        sols.forEach {
            grid[it] = Parser.Item.Outside
        }
        println(grid)
        sols.size
    }

}
