package y2023.day11

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.RunMode
import kotlin.math.abs

class Main(
    override val part1ExpectedAnswerForSample: Any = 374L,
    override val part2ExpectedAnswerForSample: Any = 8410L,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = calculateShortestDistances(Parser.parse(data, 2))
    override fun runPart2(data: List<String>, runMode: RunMode): Any = calculateShortestDistances(Parser.parse(data,
        when(runMode) {
            RunMode.Sample -> 100
            RunMode.Real -> 1000000
        }
    ))

    private fun calculateShortestDistances(grid: GenericGrid<Parser.SpaceItem>): Long {
        var total = 0L
        grid.points.keys.forEach { p1 ->
            grid.points.keys.forEach { p2 ->
                total += abs(p1.x - p2.x) + abs(p1.y - p2.y)
            }
        }
        return total / 2
    }
}
