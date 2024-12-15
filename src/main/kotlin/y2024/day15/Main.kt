package y2024.day15

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 10092L,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (grid, moves) ->
        var current = grid.points.firstNotNullOf { pair -> pair.key.takeIf { pair.value == Parser.Item.Robot } }
        moves.forEach { move ->
            var trace = current(move)
            while (true) {
                when(grid[trace]) {
                    Parser.Item.Box -> Unit
                    Parser.Item.Free -> {
                        grid[current] = Parser.Item.Free
                        val newRobot = current(move)
                        grid[newRobot] = Parser.Item.Robot
                        current = newRobot
                        if (trace != newRobot) {
                            grid[trace] = Parser.Item.Box
                        }
                        break
                    }
                    Parser.Item.Robot -> throw Exception("huh")
                    Parser.Item.Wall -> break
                }
                trace = trace(move)
            }
        }
        grid.points.filter { it.value == Parser.Item.Box }.keys.sumOf {
            it.y * 100L + it.x
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
