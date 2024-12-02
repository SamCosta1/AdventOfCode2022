package y2024.day2

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode
import kotlin.math.abs
import kotlin.math.sign

class Main(
    override val part1ExpectedAnswerForSample: Any = 2,
    override val part2ExpectedAnswerForSample: Any = 4,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { reports ->
        reports.count { report -> report.isSafe() }
    }

    private fun Report.isSafe(): Boolean {
        val deltas = this.dropLast(1).mapIndexed { index, value -> value - this[index + 1] }
        return deltas.all { abs(it) <= 3 && it != 0 } && (deltas.all { it < 0 } || deltas.all { it > 0 })
    }

    // Imma be honest this is just me being lazy
    private fun Report.isSafeWithPermutations(): Boolean {
        forEachIndexed { index, _ ->
            val new = toMutableList().apply { removeAt(index) }
            if (new.isSafe()) {
                return true
            }
        }
        return false
    }
    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { reports ->
        reports.count { report ->
            report.isSafe() || report.isSafeWithPermutations()
        }
    }
}
