package y2024.day6

import kotlinx.coroutines.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.MovementDirection
import utils.Point
import utils.RunMode
import java.util.*

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

        points.forEach { grid[it] = Parser.Item.Outside }
        return@let points.size - 1
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (startPos, grid) ->
        var currentPos = startPos(MovementDirection.North)
        var currentDirection = MovementDirection.North

        val path = mutableListOf<Point>()
        while (grid[currentPos] != Parser.Item.Outside) {
            val forwardOne = currentPos(currentDirection)
            if (grid[forwardOne] == Parser.Item.Obstacle) {
                currentDirection = currentDirection.turnRight90Degrees
                continue
            }

            if (grid[forwardOne] == Parser.Item.Outside) {
                break
            }

            path.add(forwardOne)
            currentPos = forwardOne
        }

        val sols = Collections.synchronizedCollection(mutableSetOf<Point>())
        val scope = CoroutineScope(Dispatchers.IO)

        runBlocking {
            path.map { pathStep ->
                scope.async {
                    if (checkForLooping(startPos, grid, pathStep)) {
                        sols.add(pathStep)
                    }
                }
            }.awaitAll()
            sols.size
        }
    }

    fun checkForLooping(
        startPos: Point,
        grid: GenericGrid<Parser.Item>,
        extraObstacle: Point
    ) : Boolean {
        val visited = mutableSetOf<Pair<Point, MovementDirection>>()
        var currentLoopPos = startPos
        var currentLoopDirection = MovementDirection.North
        while (grid[currentLoopPos] != Parser.Item.Outside) {
            val forwardOneLoop = currentLoopPos(currentLoopDirection)
            if (grid[forwardOneLoop] == Parser.Item.Obstacle || forwardOneLoop == extraObstacle) {
                currentLoopDirection = currentLoopDirection.turnRight90Degrees
                continue
            }

            if (grid[forwardOneLoop] == Parser.Item.Outside) {
                break
            }

            if (grid[forwardOneLoop] == Parser.Item.Free) {
                if (visited.contains(currentLoopPos to currentLoopDirection)) {
                    return true
                } else {
                    visited.add(currentLoopPos to currentLoopDirection)
                }
            }
            currentLoopPos = forwardOneLoop
        }
        return false
    }

}
