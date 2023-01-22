package y2022.day24

import y2022.day14.Day14Main.Point

class Day24Main(val file: String) {

    fun runPart2(): Int {
        val grid = Parser.parse(file)
        val outward = simulateJourney(grid, grid.startPoint, grid.exitPoint)
        val back = simulateJourney(grid, grid.exitPoint, grid.startPoint)
        val outward2 = simulateJourney(grid, grid.startPoint, grid.exitPoint)
        return outward + back + outward2
    }

    fun run(): Int {
        val grid = Parser.parse(file)
        return simulateJourney(grid, grid.startPoint, grid.exitPoint)
    }
    fun simulateJourney(grid: Grid, start: Point, end: Point): Int {

        var currentMinute = 0
        var currentPositions = setOf(start)
        while (!currentPositions.contains(end)) {
            performAllBlizzardMovement(grid)

            currentPositions = currentPositions.map { position ->
                validNextPositions(grid, start, end, position)
            }.flatten().toSet()
            currentMinute++

            currentPositions.forEach {
                grid.debugPoints[it] = '?'
            }
        }

        println("Fewest mins = $currentMinute")

        return currentMinute
    }


    private fun validNextPositions(grid: Grid, start: Point, end: Point, currentPos: Point): List<Point> {

        val possibleMoves = currentPos.possibleMoves()
        val isStayingStillAnOption = grid[currentPos].none { it is Grid.State.Blizzard }
        val validMoves = possibleMoves.filter { it == end || grid[it].isEmpty() && it != start }

        return if (isStayingStillAnOption) {
            validMoves + currentPos
        } else {
            validMoves
        }
    }

    private fun performAllBlizzardMovement(grid: Grid) {
        val alreadyMoved = mutableSetOf<Grid.State.Blizzard>()
        (0 until grid.height).forEach { y ->
            (0 until grid.width).forEach { x ->
                val set = grid[x, y]
                set.mapNotNull { state ->
                    (state as? Grid.State.Blizzard).takeUnless { alreadyMoved.contains(state) }
                }.forEach { blizzard ->
                    set.remove(blizzard)
                    val newPos = computeNewBlizzardPos(x, y, blizzard, grid)
                    grid.add(newPos, blizzard)
                    alreadyMoved.add(blizzard)
                }
            }
        }
    }

    private fun computeNewBlizzardPos(
        x: Int,
        y: Int,
        blizzard: Grid.State.Blizzard,
        grid: Grid,
    ) = Point(
        (x + blizzard.direction.xDelta).let { newX ->
            when (newX) {
                0 -> grid.width - 2
                grid.width - 1 -> 1
                else -> newX
            }
        },
        (y + blizzard.direction.yDelta).let { newY ->
            when (newY) {
                0 -> grid.height - 2
                grid.height - 1 -> 1
                else -> newY
            }
        }
    )
}
