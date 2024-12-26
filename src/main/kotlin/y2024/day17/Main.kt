package y2024.day17

import puzzlerunners.Puzzle
import utils.RunMode
import kotlin.math.pow

class Main(
    override val part1ExpectedAnswerForSample: Any = "4,6,3,5,6,3,5,2,1,0",
    override val part2ExpectedAnswerForSample: Any = 117440L,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(
        data: List<String>,
        runMode: RunMode
    ) = Parser.parse(data).let { info ->
        runProgramP1(info)
        info.output!!.joinToString(",")
    }

    private fun runProgramP1(info: Parser.Input) {
        var programIndex = 0
        while (programIndex <= info.program.lastIndex) {
            val operation = info.program[programIndex]
            val operand = info.program[programIndex + 1]
            programIndex = info.execute(operation, operand) ?: (programIndex + 2)
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        val powers = Array(info.program.size) { 0 }
        while (true) {
            val aInitial = powers.mapIndexed { index, value ->
                if (value != 0) {
                    value * 8.0.pow(index).toLong()
                } else {
                    0L
                }
            }.sum()
            val current = Parser.Input(
                regA = aInitial,
                regB = info.regB,
                regC = info.regC,
                output = mutableListOf(),
                program = info.program
            )

            runProgramP1(current)

            if (current.output.size > current.program.size) {
                throw Exception("Went too far :(")
            }
            if (current.output.size < current.program.size) {
                powers[powers.lastIndex]++
                continue
            }
            if (current.output == current.program) {
                return@let aInitial
            }
            var changed = false
            for (index in powers.lastIndex downTo 0) {
                if (changed) {
                    powers[index] = 0
                } else if (current.program[index] != current.output[index]) {
                    powers[index]++
                    changed = true
                }
            }
        }
    }
}


private fun Parser.Input.execute(operation: Long, operand: Long): Int? {
    when (operation) {
        0L -> adv(operand)
        1L -> bxl(operand)
        2L -> bst(operand)
        3L -> return jnz(operand)
        4L -> bxc(operand)
        5L -> out(operand)
        6L -> bdv(operand)
        7L -> cdv(operand)
    }
    return null
}

fun Parser.Input.division(operand: Long): Long {
    val numerator = regA
    return numerator.shr(combo(operand).toInt())
}

fun Parser.Input.adv(operand: Long) {
    regA = division(operand)
}

fun Parser.Input.bxl(operand: Long) {
    regB = regB xor operand
}

fun Parser.Input.bst(operand: Long) {
    regB = combo(operand) % 8L
}

fun Parser.Input.jnz(operand: Long): Int? {
    if (regA == 0L) {
        return null
    }
    return operand.toInt()
}

fun Parser.Input.bxc(operand: Long) {
    regB = regB xor regC
}

fun Parser.Input.out(operand: Long) {
    output?.add(combo(operand) % 8L)
}

fun Parser.Input.bdv(operand: Long) {
    regB = division(operand)
}

fun Parser.Input.cdv(operand: Long) {
    regC = division(operand)
}

private fun Parser.Input.combo(op: Long): Long = when (op) {
    in (0..3) -> op
    4L -> regA
    5L -> regB
    6L -> regC
    else -> throw Exception("Found operand $op")
}
