package y2024.day15

import puzzlerunners.Puzzle
import utils.*

class Main(
    override val part1ExpectedAnswerForSample: Any = 10092L,
    override val part2ExpectedAnswerForSample: Any = 9021L,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data, 1).let { (grid, moves) ->
        var current = grid.points.firstNotNullOf { pair -> pair.key.takeIf { pair.value == Parser.Item.Robot } }
        moves.forEach { move ->

            val firstFree = findFirstFree(current, move, grid)
            if (firstFree != null) {
                grid[current] = Parser.Item.Free
                val newRobot = current(move)
                grid[newRobot] = Parser.Item.Robot
                current = newRobot
                if (firstFree != newRobot) {
                    grid[firstFree] = Parser.Item.Box
                }
            }
        }
        grid.points.filter { it.value == Parser.Item.Box }.keys.sumOf {
            it.y * 100L + it.x
        }
    }

    private fun findFirstFree(
        current: Point,
        move: MovementDirection,
        grid: GenericGrid<Parser.Item>
    ): Point? {
        var trace = current(move)
        while (true) {
            when (grid[trace]) {
                Parser.Item.Free -> {
                    return trace
                }
                Parser.Item.Robot -> throw Exception("Found the robot")
                Parser.Item.Wall -> break
                Parser.Item.BoxLeft,
                Parser.Item.BoxRight,
                Parser.Item.Box -> Unit
            }
            trace = trace(move)
        }
        return null
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data, 2).let { (grid, moves) ->
        var current = grid.points.firstNotNullOf { pair -> pair.key.takeIf { pair.value == Parser.Item.Robot } }
        for (move in moves) {
            if (grid[current(move)] is Parser.Item.Free) {
                grid[current(move)] = Parser.Item.Robot
                grid[current] = Parser.Item.Free
                current = current(move)
                continue
            }

            if (grid[current(move)] is Parser.Item.Wall) {
                continue
            }

            if (move == MovementDirection.West || move == MovementDirection.East) {
                val firstFree = findFirstFree(current, move, grid) ?: continue
                rangeBetween(firstFree.x, current(move)(move).x).forEachIndexed { loopIndex, gridIndex ->
                    grid[Point(gridIndex, firstFree.y)] = if (loopIndex % 2 == 0) Parser.Item.BoxLeft else Parser.Item.BoxRight
                }
                grid[current(move)] = Parser.Item.Robot
                grid[current] = Parser.Item.Free
                current = current(move)
            } else {
                val pointsToMove = mutableSetOf<Point>()
                findPointsThatAreMovedBy(current(move), move, grid, pointsToMove)

                if (pointsToMove.isNotEmpty() && pointsToMove.map { it(move) }.all { pointsToMove.contains(it) || grid[it] is Parser.Item.Free }) {
                    val newPoints = mutableSetOf<Point>()
                    val oldValues = pointsToMove.associateWith { grid[it] }
                    oldValues.keys.forEach { point ->
                        grid[point(move)] = oldValues[point]!!
                        newPoints.add(point(move))
                    }
                    pointsToMove.removeAll(newPoints)
                    pointsToMove.forEach { grid[it] = Parser.Item.Free }
                    grid[current(move)] = Parser.Item.Robot
                    grid[current] = Parser.Item.Free
                    current = current(move)
                }
            }

        }
        grid.points.filter { it.value == Parser.Item.BoxLeft }.keys.sumOf {
            it.y * 100L + it.x
        }
    }
/*
    ####################
    ##[].......[].[][]##
    ##[]...........[].##
    ##[]........[][][]##
    ##[]......[]....[]##
    ##..##......[]....##
    ##..[]............##
    ##..@......[].[][]##
    ##......[][]..[]..##
    ####################
*/
    fun findPointsThatAreMovedBy(point: Point, direction: MovementDirection, grid: GenericGrid<Parser.Item>, result: MutableSet<Point>) {
        val otherBoxHalf = when (grid[point]) {
            Parser.Item.Box -> throw Exception("This shouldn't happen in part 2")
            Parser.Item.BoxLeft -> {
                point(MovementDirection.East)
            }
            Parser.Item.BoxRight -> {
                point(MovementDirection.West)
            }
            Parser.Item.Robot -> throw Exception("Found robot where robot shoudn't be")
            Parser.Item.Free -> return
            Parser.Item.Wall -> return
        }

        result.add(point)
        result.add(otherBoxHalf)

        findPointsThatAreMovedBy(point(direction), direction, grid, result)
        findPointsThatAreMovedBy(otherBoxHalf(direction), direction, grid, result)
    }
}