package y2024.day17

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode
import kotlin.math.pow

typealias NewInstructionIndex = Int
class Main(
    override val part1ExpectedAnswerForSample: Any = "4,6,3,5,6,3,5,2,1,0",
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(
        data: List<String>,
        runMode: RunMode
    ) = Parser.parse(data).let { info ->
        var programIndex = 0

        while (programIndex <= info.program.lastIndex) {
            val operation = info.program[programIndex]
            val operand = info.program[programIndex + 1]
            programIndex = info.execute(operation, operand) ?: (programIndex + 2)
        }

        info.output.joinToString(",")
    }


    private fun Parser.Input.execute(operation: Int, operand: Int): Int? {
        when (operation) {
            0 -> adv(operand)
            1 -> bxl(operand)
            2 -> bst(operand)
            3 -> return jnz(operand)
            4 -> bxc(operand)
            5 -> out(operand)
            6 -> bdv(operand)
            7 -> cdv(operand)
        }
        return null
    }

    fun Parser.Input.division(operand: Int): Int {
        val numerator = regA
        val denominator = 2.0.pow(combo(operand))
        return (numerator / denominator).toInt()
    }
    fun Parser.Input.adv(operand: Int) {
        regA = division(operand)
    }

    fun Parser.Input.bxl(operand: Int) {
        regB = regB xor operand
    }

    fun Parser.Input.bst(operand: Int) {
        regB = combo(operand) % 8
    }

    fun Parser.Input.jnz(operand: Int): Int? {
        if (regA == 0) {
            return null
        }
        return operand
    }

    fun Parser.Input.bxc(operand: Int) {
        regB = regB xor regC
    }

    fun Parser.Input.out(operand: Int) {
        output.add(combo(operand) % 8)
    }

    fun Parser.Input.bdv(operand: Int) {
        regB = division(operand)
    }

    fun Parser.Input.cdv(operand: Int) {
        regC = division(operand)
    }

    private fun Parser.Input.combo(op: Int): Int = when {
        op in (0..3) -> op
        op == 4 -> regA
        op == 5 -> regB
        op == 6 -> regC
        else -> throw Exception("Found operand $op")
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""


}
