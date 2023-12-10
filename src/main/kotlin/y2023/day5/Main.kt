package y2023.day5

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 35L,
    override val part2ExpectedAnswerForSample: Any = 46L
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        info.seeds.minOf { seed ->
            seedToSoil(seed, info)
        }
    }

    private fun seedToSoil(seed: Long, info: Parser.Info) = info.orderedMaps.fold(seed) { acc, current ->
        current(acc)
    }

    private fun seedToSoil(seeds: List<LongRange>, info: Parser.Info): List<LongRange> = info.orderedMaps.fold(seeds) { acc, current ->
        current(acc.sortedBy { it.first })
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        info.seeds.asSequence().chunked(2).map {
            (it.first() until  it.first() + it.last())
        }.map { range ->
            seedToSoil(listOf(range), info)
        }.flatten().minOf { it.first }
    }
}
