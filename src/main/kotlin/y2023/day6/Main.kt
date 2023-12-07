package y2023.day6

import utils.Puzzle
import utils.RunMode
import y2022.day19.productOf

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).productOf {
        it.numberOfWaysToWin()
    }

    private fun Parser.Race.numberOfWaysToWin() = (0..timeMs).count {
        (timeMs - it) * it > distanceMm
    }

    override fun runPart2(data: List<String>, runMode: RunMode)= Parser.parsePart2(data).numberOfWaysToWin()
}
