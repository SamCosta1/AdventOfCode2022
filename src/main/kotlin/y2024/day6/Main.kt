package y2024.day6

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.MovementDirection
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 41,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (startPos, grid) ->
        var nextPos = startPos
        var direction = MovementDirection.North
        val points = mutableSetOf(startPos)
        while (grid[nextPos] != Parser.Item.Outside) {
            if (grid[nextPos(direction)] == Parser.Item.Obstacle) {
                direction = direction.turnRight90Degrees
            } else {
                nextPos = nextPos(direction)
                points.add(nextPos)
            }
        }

        return@let points.size - 1
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
