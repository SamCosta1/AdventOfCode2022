package y2023.day10

import utils.Point
import utils.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (grid, startPoint) ->
        var loopLength = 0L
        followLoop(grid, startPoint) { loopLength++ }
        loopLength / 2
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (grid, startPoint) ->
        val loop = mutableSetOf<Point>()
        loop.add(startPoint)
        followLoop(grid, startPoint, loop::add)

        val maxPoint = grid.bottomRightMostPoint.let { Point(it.x + 1, it.y + 1) }
        val outsideTheLoopPoints = mutableSetOf<Point>().also {
            shrinkWrap(Point(-1, -1), maxPoint, it, loop)
        }

        val insideTiles = mutableSetOf<Point>()
        for (x in 0..grid.bottomRightMostPoint.x) {
            for (y in 0..grid.bottomRightMostPoint.y) {
                val point = Point(x, y)
                if (!outsideTheLoopPoints.contains(point)
                    && !loop.contains(point)) {
                    insideTiles.add(point)
                }
            }
        }
        outsideTheLoopPoints.forEach {
            grid[it] = Parser.PipeItem(char = "O")
        }
        insideTiles.forEach {
            grid[it] = Parser.PipeItem(char = "I")
        }
        println(grid)

        insideTiles.size
    }

    fun shrinkWrap(thisPoint: Point, maxPoint: Point, allPoints: MutableSet<Point>, loopPoints: Set<Point>) {
        allPoints.add(thisPoint)

        thisPoint.adjacentPoints().filter {
            it.x >= -1
                    && it.y >= -1
                    && it.x <= maxPoint.x
                    && it.y <= maxPoint.y
                    && !allPoints.contains(it)
                    && !loopPoints.contains(it)
        }.forEach {
            shrinkWrap(it, maxPoint, allPoints, loopPoints)
        }
    }
}
