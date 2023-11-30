package y2021.day1

import utils.Puzzle
import utils.RunMode

class Main(override val part1ExpectedAnswerForSample: Any, override val part2ExpectedAnswerForSample: Any) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.map { it.toInt() }.let { measurments ->
        (1..measurments.lastIndex).count { index ->
            measurments[index] > measurments[index - 1]
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = data.map { it.toInt() }.let { measurments ->
        (2 until measurments.lastIndex).count { index ->
            val previousSum = measurments[index - 2] + measurments[index -1] + measurments[index]
            val thisSum = measurments[index - 1] + measurments[index] + measurments[index + 1]

            thisSum > previousSum
        }
    }
}