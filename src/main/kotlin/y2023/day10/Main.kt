package y2023.day10

import utils.GenericGrid
import utils.Point
import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 8L,
    override val part2ExpectedAnswerForSample: Any = 10
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (grid, startPoint) ->
        var loopLength = 0L
        followLoop(grid, startPoint) { loopLength++ }
        loopLength / 2
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (grid, startPoint) ->
        val loop = mutableSetOf<Point>()
        loop.add(startPoint)
        followLoop(grid, startPoint, loop::add)

        val insideTiles = mutableSetOf<Point>()
        for (x in 0..grid.bottomRightMostPoint.x) {
            for (y in 0..grid.bottomRightMostPoint.y) {
                val point = Point(x, y)
                if (point.isInsideLoop(grid, loop)) {
                    insideTiles.add(point)
                }
            }
        }

        // This is just for debugging
        insideTiles.forEach {
            grid[it] = Parser.PipeItem("I")
        }

        insideTiles.size
    }

    private fun Point.isInsideLoop(grid: GenericGrid<Parser.PipeItem>, loop: Set<Point>): Boolean {
        if (loop.contains(this)) {
            return false
        }

        // Count the number of times a line projected across the bottom half of the cells crosses over the loop
        val pipesThatBlockBottom = setOf("│", "┐", "┌")
        return (x..grid.bottomRightMostPoint.x + 2)
            .asSequence()
            .map { Point(it, y) }
            .filter { loop.contains(it) }
            .map { grid[it] }
            .filter { pipesThatBlockBottom.contains(it.char) }
            .count() % 2 == 1
    }
}
