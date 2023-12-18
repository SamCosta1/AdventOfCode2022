package y2023.day18

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.MovementDirection
import utils.Point
import utils.RunMode
import java.awt.PointerInfo
import java.nio.file.Files
import java.nio.file.Path

class Main(
    override val part1ExpectedAnswerForSample: Any = 62,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { input ->
        val grid = GenericGrid<Parser.InputLine>(Parser.InputLine(MovementDirection.South, -1, ""))

        val trenches = input.toSet()

        var currentPos = when (runMode) {
            RunMode.Sample -> Point(0, 0)
            RunMode.Real -> Point(188, 226)
        }

        val horizontalLines = mutableListOf<Pair<Point, Point>>()
        input.forEach { line ->
            val start = currentPos
//            (0..line.amount).forEach { amount ->
//                grid[start(line.direction, amount)] = line
//            }
            if (line.direction in listOf(MovementDirection.East, MovementDirection.West)) {
                horizontalLines.add(Pair(start, start(line.direction, line.amount)))
            }
            currentPos = start(line.direction, line.amount)
        }

        val organisedLines = horizontalLines.map {
            if (it.first.x < it.second.x) {
                it
            } else {
                Pair(it.second, it.first)
            }
        }.sortedBy { it.first.y }.asSequence()

        var count = 0
        (grid.topLeftMostPoint.y..grid.bottomRightMostPoint.y).forEach { y ->
            (grid.topLeftMostPoint.x..grid.bottomRightMostPoint.x).forEach { x ->
                if (grid[x, y].isTrench) {
                    count++
                } else {
                    val crossings =
                        organisedLines.filter { x >= it.first.x && x < it.second.x && y < it.first.y }.count()
                    if (crossings % 2 == 1) {
                        count++
                    }

                }
            }
        }
        Files.writeString(
            Path.of("/Users/samdc/Projects/AdventOfCode/AdventOfCode2022-2/src/main/kotlin/y2023/day18/test.txt"),
            grid.toString()
        )
        count
    }


    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
