package y2024.day7

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 3749L,
    override val part2ExpectedAnswerForSample: Any = 11387L,
    override val isComplete: Boolean = true
) : Puzzle {

    override fun runPart1(data: List<String>, runMode: RunMode): Any = Parser.parse(data).sumValidEquations(listOf('*', '+'))
    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).sumValidEquations(listOf('*', '+', '|'))


    private fun List<Parser.Equation>.sumValidEquations(operators: List<Char>) = sumOf { (target, operands) ->
        if (branchAndBound(target, operands.first(), operands.subList(1, operands.size), operators)) {
            target
        } else {
            0L
        }
    }
    private fun branchAndBound(target: Long, lastValue: Long, operands: List<Long>, operators: List<Char>): Boolean {
        if (operands.isEmpty()) {
            return target == lastValue
        }
        return operators.any { operator ->
            val nextValue = when (operator) {
                '*' -> lastValue * operands.first()
                '+' -> lastValue + operands.first()
                '|' -> "$lastValue${operands.first()}".toLong()
                else -> throw Exception("eh")
            }

            if (nextValue <= target) {
                branchAndBound(target, nextValue, operands.subList(1, operands.size), operators)
            } else {
                false
            }
        }
    }
}
