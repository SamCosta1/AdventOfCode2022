package y2021.day1

import utils.Puzzle
import utils.RunMode

class Main: Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.map { it.toInt() }.let { measurments ->
        (1..measurments.lastIndex).count { index ->
            measurments[index] > measurments[index - 1]
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}