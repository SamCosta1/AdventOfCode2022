package y2023.day16

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.MovementDirection
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 46,
    override val part2ExpectedAnswerForSample: Any = 51,
    override val isComplete: Boolean = false,
) : Puzzle {

    data class BeamLocation(val direction: MovementDirection, val position: Point)

    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val beamLocations = mutableListOf(BeamLocation(MovementDirection.East, Point(-1, 0)))
        calculateNumberEnergised(beamLocations, grid)
    }

    private fun calculateNumberEnergised(
        beamLocations: MutableList<BeamLocation>,
        grid: GenericGrid<Parser.Tile>
    ): Int {
        val energised = mutableSetOf<BeamLocation>()
        val sidysidy = listOf(MovementDirection.West, MovementDirection.East)
        val upydowny = listOf(MovementDirection.North, MovementDirection.South)

        while (beamLocations.isNotEmpty()) {
            val iterator = beamLocations.listIterator()
            while (iterator.hasNext()) {
                val location = iterator.next()

                if (energised.contains(location)) {
                    iterator.remove()
                    continue
                }

                val newPos = location.position(location.direction)

                if (grid[location.position] != Parser.Tile.offGrid) {
                    energised.add(location)
                }

                when (grid[newPos].type) {
                    '.' -> location.copy(position = newPos).let { listOf(it) }
                    '/' -> BeamLocation(
                        when (location.direction) {
                            MovementDirection.North -> MovementDirection.East
                            MovementDirection.South -> MovementDirection.West
                            MovementDirection.West -> MovementDirection.South
                            MovementDirection.East -> MovementDirection.North
                        }, newPos
                    ).let { listOf(it) }

                    '\\' -> BeamLocation(
                        when (location.direction) {
                            MovementDirection.North -> MovementDirection.West
                            MovementDirection.South -> MovementDirection.East
                            MovementDirection.East -> MovementDirection.South
                            MovementDirection.West -> MovementDirection.North
                        }, newPos
                    ).let { listOf(it) }

                    '|' -> {
                        if (location.direction in sidysidy) {
                            listOf(
                                BeamLocation(MovementDirection.South, newPos),
                                BeamLocation(MovementDirection.North, newPos)
                            )
                        } else {
                            location.copy(position = newPos).let { listOf(it) }
                        }
                    }

                    '-' -> if (location.direction in upydowny) {
                        listOf(
                            BeamLocation(MovementDirection.East, newPos),
                            BeamLocation(MovementDirection.West, newPos)
                        )
                    } else {
                        location.copy(position = newPos).let { listOf(it) }
                    }

                    Parser.Tile.offGrid.type -> {
                        emptyList<BeamLocation>()
                    }

                    else -> throw Exception("oops")
                }.also {
                    iterator.remove()
                    it.forEach(iterator::add)
                }
            }
        }

        return energised.distinctBy { it.position }.size
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        var currentMax: Int = 0
        (grid.topLeftMostPoint.x..grid.bottomRightMostPoint.x).forEach { x ->
            currentMax = maxOf(
                calculateNumberEnergised(
                    mutableListOf(BeamLocation(MovementDirection.South, Point(x, -1))),
                    grid,
                ),
                calculateNumberEnergised(
                    mutableListOf(BeamLocation(MovementDirection.North, Point(x, grid.bottomRightMostPoint.y + 1))),
                    grid,
                ),
                currentMax
            )
        }

        (grid.topLeftMostPoint.y..grid.bottomRightMostPoint.y).forEach { y ->
            currentMax = maxOf(
                calculateNumberEnergised(
                    mutableListOf(BeamLocation(MovementDirection.East, Point(-1, y))),
                    grid,
                ),
                calculateNumberEnergised(
                    mutableListOf(BeamLocation(MovementDirection.West, Point(grid.bottomRightMostPoint.x + 1, y))),
                    grid,
                ),
                currentMax
            )
        }

        currentMax
    }
}
