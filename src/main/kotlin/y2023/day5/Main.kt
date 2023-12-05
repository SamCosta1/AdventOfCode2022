package y2023.day5

import utils.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        info.seeds.minOf { seed ->
            seedToSoil(seed, info)
        }
    }

    private fun seedToSoil(seed: Long, info: Parser.Info) = info.seedToSoil(seed)
        .let { info.soilToFert(it) }//.also { println("soilToFert  $it")}
        .let { info.fertToWater(it) }//.also { println("fertToWater  $it")}
        .let { info.waterToLight(it) }//.also { println("waterToLight  $it")}
        .let { info.lightToTemp(it) }//.also { println("lightToTemp  $it")}
        .let { info.tempToHum(it) }//.also { println("tempToHum  $it")}
        .let { info.humToLocation(it) }//.also { println("humToLocation  $it")}

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->

        info.seeds.chunked(2).map {
            (it.first() until it.first() + it.last())
        }.minOf {
            it.minOf { seed ->
                seedToSoil(seed, info)
            }
        }

    }
}
