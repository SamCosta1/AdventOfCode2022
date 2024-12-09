package y2024.day9

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 1928L,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.first().map { it.digitToInt() }.toIntArray().let { seq ->
        var forwardIndex = 0
        var backwardIndex = seq.lastIndex
        var checksum = 0L

        var endArrIndex = 0
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
                checksum += idIndex * endArrIndex.toLong()
                seq[forwardIndex]--
            } else {
                seq[forwardIndex]--
                seq[backwardIndex]--
                val idIndex = backwardIndex / 2
                checksum += idIndex * endArrIndex.toLong()
            }
            endArrIndex++
        }

        checksum
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
