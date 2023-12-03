package y2023.day3

import utils.Puzzle
import utils.RunMode
import y2022.day19.productOf
import y2022.day19.productOfLong
import y2022.day23.adjacentPoints

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        grid.points.filter { entry ->
            entry.key.adjacentPoints().any { grid[it] is Parser.Grid.State.Symbol }
        }.mapNotNull { it.value as? Parser.Grid.State.Number }
            .distinctBy { it.id }
            .sumOf { it.value }
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        grid.points.mapNotNull { entry ->
            if (entry.value is Parser.Grid.State.Symbol && entry.value.char == "*") {
                entry.key.adjacentPoints().mapNotNull {
                    grid[it] as? Parser.Grid.State.Number
                }.distinctBy { it.id }
                    .takeIf { it.size == 2 }?.productOfLong { it.value }
            } else {
                null
            }
        }.sumOf { it  }
    }

}
