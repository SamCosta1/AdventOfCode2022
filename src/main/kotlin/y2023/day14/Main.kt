package y2023.day14

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.MovementDirection
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 136L,
    override val part2ExpectedAnswerForSample: Any = 64L,
    override val isComplete: Boolean = false
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any =
        Parser.parse(data).let { (grid, roundRockStartPoints) ->
            // roundRockStartPoints already in order of the top most ones first
            executeStep(MovementDirection.North, grid)
            grid.calculateRoundRockLoad()
        }

    override fun runPart2(
        data: List<String>,
        runMode: RunMode
    ) = Parser.parse(data).let { (grid, _) ->
        val directions =
            listOf(MovementDirection.North, MovementDirection.West, MovementDirection.South, MovementDirection.East)
        val snapshots = mutableListOf<String>()
        val loads = mutableListOf<Long>()

        val totalIterations = 1000000000
        var answer: Long? = null
        var loopIndex = 0
        while (answer == null && loopIndex++ < totalIterations) {
            directions.forEach { direction ->
                executeStep(direction, grid)
            }
            val snap = grid.toString()

            if (!snapshots.contains(snap)) {
                snapshots.add(snap)
                loads.add(grid.calculateRoundRockLoad())
            } else {
                val firstLoopStart = snapshots.indexOf(snap)

                val loopLoads = loads.drop(firstLoopStart)

                val index = (totalIterations - firstLoopStart - 1) % loopLoads.size
                answer = loopLoads[index]

            }
        }

        answer ?: throw Exception("No answer found")
    }

    private fun GenericGrid<Parser.Rock>.calculateRoundRockLoad() =
        points.filter { it.value == Parser.Rock.Round }.keys.sumOf { point ->
            bottomRightMostPoint.y - point.y + 1
        }

    private fun executeStep(direction: MovementDirection, grid: GenericGrid<Parser.Rock>) {
        grid.points.asSequence().filter { it.value == Parser.Rock.Round }.map { it.key }.sortedBy {
            when (direction) {
                MovementDirection.North -> it.y
                MovementDirection.South -> -it.y
                MovementDirection.West -> it.x
                MovementDirection.East -> -it.x
            }
        }.forEachIndexed { _, rockPoint ->
            var point = rockPoint
            while (grid[point(direction)] == Parser.Rock.None) {
                grid[point(direction)] = Parser.Rock.Round
                grid[point] = Parser.Rock.None
                point = point(direction)
            }
        }
    }
}
