package y2022.day23

import y2022.day15.Point

enum class MovementDirection {
    North,
    South,
    West,
    East
}

class Day23Main(val file: String) {

    data class Result(val finalRoundNumber: Int)
    fun run(maxRounds: Int = 10): Long {
        val grid = Parser.parse(file)
        val result = runRounds(grid, maxRounds)

        val numElves = grid.points.values.count { it is Grid.State.Elf }
        val smallestRectangleSize = (grid.bottomRightMostPoint.x + 1 - grid.topLeftMostPoint.x) * (grid.bottomRightMostPoint.y + 1 - grid.topLeftMostPoint.y)

        return smallestRectangleSize - numElves
    }

    fun runPart2(): Int {
        val grid = Parser.parse(file)
        return runRounds(grid, Int.MAX_VALUE - 1).finalRoundNumber
    }

    private fun runRounds(grid: Grid, maxRound: Int): Result {
        val directions = MovementDirection.values()
        var directionStartIndex = 0

        repeat(maxRound) { round ->
            val newPoints = mutableMapOf<Point, Point>() // Elf -> new position
            val clashes = mutableMapOf<Point, Int>() // new point -> number of times used
            grid.points.forEach { (position, elf) ->
                if (elf is Grid.State.Elf) {
                    val newPos = newPosition(grid, elf, directionStartIndex, directions)

                    if (position != newPos) {
                        newPoints[position] = newPos
                        clashes[newPos] = (clashes[newPos] ?: 0) + 1
                    }
                }
            }

            if (newPoints.isEmpty()) {
                return Result(round + 1)
            }

            newPoints.forEach { (elfPos, newPoint) ->
//                println("${elf.currentPosition} -> $newPoint")
                if ((clashes[newPoint] ?: throw Exception("Something's broke")) == 1) {
                    val elf = grid[elfPos] as Grid.State.Elf
                    grid[elfPos] = Grid.State.Air
                    elf.currentPosition = newPoint
                    if (grid[newPoint] is Grid.State.Elf) {
                        throw Exception("Elf overlap")
                    }
                    grid[newPoint] = elf
                }
            }

//            println(clashes)
            directionStartIndex += 1

//            println()
//            println("End of round ${it+1}  Number of elves ${grid.points.values.count { it is Grid.State.Elf }}")
//            println(grid)
        }


        return Result(-1)
    }

    private fun newPosition(
        grid: Grid,
        elf: Grid.State.Elf,
        directionStartIndex: Int,
        directions: Array<MovementDirection>
    ): Point {
        if (elf.currentPosition.adjacentPoints().all { grid[it] is Grid.State.Air }) {
            return elf.currentPosition
        }

        var newPosition = elf.currentPosition
        var directionsTried = 0
        while (directionsTried < 4 && newPosition == elf.currentPosition) {
            val direction = directions[(directionStartIndex + directionsTried) % directions.size]

            if (elf.currentPosition.adjacentPointsFor(direction).all { grid[it] == Grid.State.Air }) {
                newPosition = elf.currentPosition.newPointForMovement(direction)
            }

            directionsTried++
        }

        return newPosition
    }
}

private fun Point.adjacentPoints() = listOf(
    Point(x - 1, y - 1),
    Point(x - 1, y),
    Point(x - 1, y + 1),
    Point(x + 1, y + 1),
    Point(x + 1, y),
    Point(x + 1, y - 1),
    Point(x, y + 1),
    Point(x, y - 1)
)

private fun Point.adjacentPointsFor(movementDirection: MovementDirection) = listOfNotNull(
    Point(x - 1, y - 1).takeIf { movementDirection in listOf(MovementDirection.North, MovementDirection.West) },
    Point(x - 1, y).takeIf { movementDirection in listOf(MovementDirection.West) },
    Point(x - 1, y + 1).takeIf { movementDirection in listOf(MovementDirection.South, MovementDirection.West) },
    Point(x, y + 1).takeIf { movementDirection in listOf(MovementDirection.South) },
    Point(x + 1, y + 1).takeIf { movementDirection in listOf(MovementDirection.South, MovementDirection.East) },
    Point(x + 1, y).takeIf { movementDirection in listOf(MovementDirection.East) },
    Point(x + 1, y - 1).takeIf { movementDirection in listOf(MovementDirection.North, MovementDirection.East) },
    Point(x, y - 1).takeIf { movementDirection in listOf(MovementDirection.North) },
)

private fun Point.newPointForMovement(movementDirection: MovementDirection) = when (movementDirection) {
    MovementDirection.North -> Point(x, y - 1)
    MovementDirection.South -> Point(x, y + 1)
    MovementDirection.West -> Point(x - 1, y)
    MovementDirection.East -> Point(x + 1, y)
}