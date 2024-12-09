package y2024.day9

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 1928L,
    override val part2ExpectedAnswerForSample: Any = 2858L,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) =
        data.first().map { it.digitToInt() }.toIntArray().let { seq ->
            var forwardIndex = 0
            var backwardIndex = seq.lastIndex
            var checksum = 0L

            var outputArrIndex = 0
            while (forwardIndex <= backwardIndex) {
                if (seq[forwardIndex] <= 0) {
                    forwardIndex++
                    continue
                }
                if (seq[backwardIndex] <= 0) {
                    backwardIndex -= 2
                    continue
                }

                if (forwardIndex % 2 == 0) {
                    val idIndex = forwardIndex / 2
                    checksum += idIndex * outputArrIndex.toLong()
                    seq[forwardIndex]--
                } else {
                    seq[forwardIndex]--
                    seq[backwardIndex]--
                    val idIndex = backwardIndex / 2
                    checksum += idIndex * outputArrIndex.toLong()
                }
                outputArrIndex++
            }

            checksum
        }

    data class Cell(val originalValue: Int, var newValue: Int, val nowOccupying: MutableMap<Int, Int> =  mutableMapOf()) {
        override fun toString(): String {
            return "$originalValue->$newValue | $nowOccupying"
        }
    }
    override fun runPart2(data: List<String>, runMode: RunMode) =
        data.first().map { Cell(it.digitToInt(), it.digitToInt()) }.toTypedArray().let { seq ->
            var checksum = 0L

            var outputArrIndex = 0
            for (backwardIndex in seq.lastIndex downTo 1 step 2) {
                for (i in 1..<backwardIndex step 2) {
                    if (seq[i].newValue >= seq[backwardIndex].originalValue) {
                        seq[i].newValue -= seq[backwardIndex].originalValue
                        seq[i].nowOccupying[backwardIndex / 2] = seq[backwardIndex].originalValue
                        seq[backwardIndex].newValue = 0
                        break
                    }
                }
            }

            for (forwardIndex in 0..seq.lastIndex) {
                if (forwardIndex % 2 == 0) {
                    val id = forwardIndex / 2L
                    repeat(seq[forwardIndex].newValue) {
                        checksum += outputArrIndex * id
                        outputArrIndex++
                    }
                    if (seq[forwardIndex].newValue == 0) {
                        repeat(seq[forwardIndex].originalValue) {
                            outputArrIndex++
                        }
                    }
                } else {
                    var totalSpaces = seq[forwardIndex].originalValue
                    seq[forwardIndex].nowOccupying.forEach { (id, numberSpaces) ->
                        repeat(numberSpaces) {
                            checksum += outputArrIndex * id
                            outputArrIndex++
                            totalSpaces--
                        }
                    }
                    repeat(totalSpaces) {
                        outputArrIndex++
                    }
                }
            }

            checksum
        }
}
