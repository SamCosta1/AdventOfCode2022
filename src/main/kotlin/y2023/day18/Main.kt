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
    override val part1ExpectedAnswerForSample: Any = 62L,
    override val part2ExpectedAnswerForSample: Any = 952408144115L,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = execute(runMode, Parser.parse(data))

    private fun execute(runMode: RunMode, input: List<Parser.InputLine>): Long {
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

        var count = 0L
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
        return count
    }

    private fun shoeLace(runMode: RunMode, input: List<Parser.InputLine>): Long {
        val grid = GenericGrid<Parser.InputLine>(Parser.InputLine(MovementDirection.North, -1, ""))
        var currentPos = when (runMode) {
            RunMode.Sample -> Point(0, 0)
            RunMode.Real -> Point(188, 226)
        }


        var last = currentPos
        val verticies = mutableSetOf(currentPos)
        input.mapIndexed { index, line ->
            val nextLine = input[(index + 1) % input.size]
            val next = currentPos(
                line.direction,
                line.amount
            )

            // Top right corner
            if (line.direction == MovementDirection.East && nextLine.direction == MovementDirection.South) {
                verticies.add(next.copy(x = next.x + 1))
            }

            if (line.direction == MovementDirection.South && nextLine.direction == MovementDirection.West) {
//                verticies.add(verticies.last().let {  })
            }

            currentPos = next

        }


//        var total = 0L
//        verticies.dropLast(1).forEachIndexed { index, point ->
//            val p1 = point
//            val p2 = verticies[(index + 1) % verticies.size]
//
//            total += (p1.x * p2.y) - (p2.x * p1.y)
//        }

//        verticies.forEachIndexed { index, it ->
//            grid[it] = Parser.InputLine(MovementDirection.North, 400L + index, "")
//        }
//        println(grid)
        println(verticies)
        println(verticies.map { it.copy(y = 12 - it.y) })

        return 0L
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
//            if (y < horizontalLine.first.y) {
//                println("$y $horizontalLine")
//                return false
//            }
        }
        for (verticalLine in verticalLines) {
            if (x == verticalLine.first.x && y in verticalLine.first.y..verticalLine.second.y) {
                return true
            }
//            if (x > verticalLine.first.x) {
//                return false
//            }
        }
        return false
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = execute(runMode, Parser.parse(data).map {
//        val amount = it.hash.dropLast(1).drop(1).toLong(radix = 16)
//        val direction = when (it.hash.last()) {
//            '0' -> MovementDirection.East
//            '1' -> MovementDirection.South
//            '2' -> MovementDirection.West
//            '3' -> MovementDirection.North
//            else -> throw Exception()
//        }
//        Parser.InputLine(direction, amount, "")
        it
    })
}
