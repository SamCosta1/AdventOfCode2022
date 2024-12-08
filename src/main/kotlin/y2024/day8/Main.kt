package y2024.day8

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.GenericGrid
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 14,
    override val part2ExpectedAnswerForSample: Any = 34,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(
        data: List<String>, runMode: RunMode
    ) = Parser.parse(data).loopAndCountAntNodes(::computeAntiNodesPart1)

    override fun runPart2(
        data: List<String>, runMode: RunMode
    ) = Parser.parse(data).loopAndCountAntNodes(::computeAntiNodesPart2)

    private fun GenericGrid<Parser.Item>.loopAndCountAntNodes(
        computeAntiNodes: (
            p1: Point, p2: Point, grid: GenericGrid<Parser.Item>
        ) -> Sequence<Point>
    ): Int {
        val antennas = points.toList().groupBy { it.second }.filter { it.key is Parser.Item.Antenna }
            .mapValues { values -> values.value.map { it.first } }
        val results = mutableSetOf<Point>()
        antennas.forEach { (_, locations) ->
            locations.forEach { p1 ->
                locations.asSequence().filter { it != p1 }.forEach { p2 ->
                    results.addAll(computeAntiNodes(p1, p2, this))
                }
            }
        }
        return results.size
    }

    private fun computeAntiNodesPart1(p1: Point, p2: Point, grid: GenericGrid<Parser.Item>): Sequence<Point> {
        val diff = p1 - p2
        return sequenceOf(p1 + diff, p2 - diff).filter { grid[it] != Parser.Item.Outside }
    }

    private fun computeAntiNodesPart2(p1: Point, p2: Point, grid: GenericGrid<Parser.Item>): Sequence<Point> {
        val diff = p1 - p2
        return sequence {
            var current = p2 + diff
            while (grid[current] != Parser.Item.Outside) {
                yield(current)
                current += diff
            }

            current = p2 - diff
            while (grid[current] != Parser.Item.Outside) {
                yield(current)
                current -= diff
            }
        }
    }
}

