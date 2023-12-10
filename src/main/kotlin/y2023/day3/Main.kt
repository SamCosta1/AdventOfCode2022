package y2023.day3

import puzzlerunners.Puzzle
import utils.RunMode
import y2022.day19.productOfLong

class Main(
    override val part1ExpectedAnswerForSample: Any = 4361L,
    override val part2ExpectedAnswerForSample: Any = 467835L
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        grid.points.filter { entry ->
            entry.key.adjacentPoints().any { grid[it] is Parser.GridItem.Symbol }
        }.mapNotNull { it.value as? Parser.GridItem.Number }
            .distinctBy { it.id }
            .sumOf { it.value }
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        grid.points.mapNotNull { entry ->
            if (entry.value is Parser.GridItem.Symbol && entry.value.char == "*") {
                entry.key.adjacentPoints().mapNotNull {
                    grid[it] as? Parser.GridItem.Number
                }.distinctBy { it.id }
                    .takeIf { it.size == 2 }?.productOfLong { it.value }
            } else {
                null
            }
        }.sumOf { it  }
    }

}
