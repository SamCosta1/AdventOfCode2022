package y2021.day3

import utils.BinaryUtils
import utils.Puzzle
import utils.RunMode

class Main(override val part1ExpectedAnswerForSample: Any, override val part2ExpectedAnswerForSample: Any) : Puzzle {
    data class MostAndLeastCommon(val most: List<Int>, val least: List<Int>)
    private fun List<List<Int>>.mostAndLeastCommon(): MostAndLeastCommon {
        val onesCount = IntArray(first().count()) { 0 }

        forEach { binary ->
            binary.forEachIndexed { index, thisNumber ->
                onesCount[index] += thisNumber
            }
        }

        val most = onesCount.map {
            if (it >= size - it) {
                1
            } else {
                0
            }
        }
        val least = onesCount.map {
            if (size - it > it) {
                1
            } else {
                0
            }
        }
        return MostAndLeastCommon(most, least)
    }
    override fun runPart1(data: List<String>, runMode: RunMode) = data.map { raw -> raw.map { it.toString().toInt() } }.let { raw ->
        val result = raw.mostAndLeastCommon()
        BinaryUtils.fromBinary(result.most) * BinaryUtils.fromBinary(result.least)
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = data.map { raw -> raw.map { it.toString().toInt() } }.let { raw ->
        var currentO2List = raw
        var oxygen = 0.0
        (0..raw.first().size).forEach { currentBitIndex ->
            if (currentO2List.size > 1) {
                val mostAndLeastCommon = currentO2List.mostAndLeastCommon()
                currentO2List = currentO2List.filter { it[currentBitIndex] == mostAndLeastCommon.most[currentBitIndex] }

                if (currentO2List.size == 1) {
                    oxygen = BinaryUtils.fromBinary(currentO2List.first())
                }
            }
        }

        var currentCO2List = raw
        var co2 = 0.0
        (0..raw.first().size).forEach { currentBitIndex ->
            if (currentCO2List.size > 1) {
                val mostAndLeastCommon = currentCO2List.mostAndLeastCommon()
                currentCO2List = currentCO2List.filter { it[currentBitIndex] == mostAndLeastCommon.least[currentBitIndex] }

                if (currentCO2List.size == 1) {
                    co2 = BinaryUtils.fromBinary(currentCO2List.first())
                }
            }
        }

        co2 * oxygen
    }
}
