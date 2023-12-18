package y2023.day18

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.MovementDirection
import utils.Point
import utils.RunMode
import java.nio.file.Files
import java.nio.file.Path

class Main(
    override val part1ExpectedAnswerForSample: Any = 62,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { input ->
//        val grid = GenericGrid<Parser.InputLine>(Parser.InputLine(MovementDirection.South, -1, ""))


        var currentPos = when (runMode) {
            RunMode.Sample -> Point(0, 0)
            RunMode.Real -> Point(188, 226)
        }

        val _horizontalLines = mutableListOf<Pair<Point, Point>>()
        val _verticalLines = mutableListOf<Pair<Point, Point>>()
        input.forEach { line ->
            val start = currentPos
            (0..line.amount).forEach { amount ->
//                grid[start(line.direction, amount)] = line
            }
            if (line.direction in listOf(MovementDirection.East, MovementDirection.West)) {
                _horizontalLines.add(Pair(start, start(line.direction, line.amount)))
            }
            if (line.direction in listOf(MovementDirection.North, MovementDirection.South)) {
                _verticalLines.add(Pair(start, start(line.direction, line.amount)))
            }
            currentPos = start(line.direction, line.amount)
        }

        val horizontalLines = _horizontalLines.map {
            if (it.first.x < it.second.x) {
                it
            } else {
                Pair(it.second, it.first)
            }
        }.sortedBy { it.first.y }.asSequence()

        val verticalLines = _verticalLines.map {
            if (it.first.y < it.second.y) {
                it
            } else {
                Pair(it.second, it.first)
            }
        }.sortedBy { it.first.x }.asSequence()

        var count = 0
        val allLinePoints = (horizontalLines.map {
            listOf(it.first, it.second)
        } + verticalLines.map {
            listOf(it.first, it.second)
        }).flatten()

        val sortByX = allLinePoints.sortedBy { it.x }
        val sortByY = allLinePoints.sortedBy { it.y }
        val topLeft = Point(sortByX.first().x, sortByY.first().y)
        val bottomRight = Point(sortByX.last().x, sortByY.last().y)

        (topLeft.y..bottomRight.y).forEach { y ->
            (topLeft.x..bottomRight.x).forEach { x ->
                if (isInLine(x, y, horizontalLines, verticalLines)) {
                    count++
                } else {
                    val crossings = horizontalLines.filter {
                        x >= it.first.x && x < it.second.x && y < it.first.y
                    }.count()

                    if (crossings % 2 == 1) {
                        count++
//                        grid[x, y] = Parser.InputLine(MovementDirection.North, 400, "")
                    }

                }
            }
        }
//        println(grid)
//        Files.writeString(
//            Path.of("/Users/samdc/Projects/AdventOfCode/AdventOfCode2022-2/src/main/kotlin/y2023/day18/test.txt"),
//            grid.toString()
//        )
        count
    }


    fun isInLine(
        x: Long,
        y: Long,
        horizontalLines: Sequence<Pair<Point, Point>>,
        verticalLines: Sequence<Pair<Point, Point>>
    ): Boolean {
        for (horizontalLine in horizontalLines) {
            if (y == horizontalLine.first.y && x in horizontalLine.first.x..horizontalLine.second.x) {
                return true
            }
            if (y > horizontalLine.first.y) {
//                return false
            }
        }
        for (verticalLine in verticalLines) {
            if (x == verticalLine.first.x && y in verticalLine.first.y..verticalLine.second.y) {
                return true
            }
            if (x > verticalLine.first.x) {
//                return false
            }
        }
        return false
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
