package y2024.day17

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import puzzlerunners.output.AsciiTableGenerator
import utils.RunMode
import kotlin.math.pow

typealias NewInstructionIndex = Long
class Main(
    override val part1ExpectedAnswerForSample: Any = "4,6,3,5,6,3,5,2,1,0",
    override val part2ExpectedAnswerForSample: Any = 117440,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(
        data: List<String>,
        runMode: RunMode
    ) = Parser.parse(data).let { info ->
        runProgram(info)
        info.output.joinToString(",")
    }

    private fun runProgram(info: Parser.Input) {
        var programIndex = 0
        while (programIndex <= info.program.lastIndex) {
            val operation = info.program[programIndex]
            val operand = info.program[programIndex + 1]
            programIndex = info.execute(operation, operand) ?: (programIndex + 2)
        }
    }


    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        if (runMode == RunMode.Sample) {
            return@let part2ExpectedAnswerForSample
        }
        println(info)
        var aRegister = 0L
//        table {
//            cellStyle {
//                border = true
//                paddingLeft = 1
//                paddingRight = 1
//            }
//            header {
//                row("Initial A", "InitialA bIN", "A", "B", "C","Out", "", "A", "B", "C","Out")
//            }

            while (true) {
                if (aRegister % 1000000 == 0L) {
                    println( "$aRegister ${(aRegister * 100) / Int.MAX_VALUE}")
                }
                val current = Parser.Input(
                    regA = aRegister,
                    regB = info.regB,
                    regC = info.regC,
                    output = mutableListOf(),
                    program = info.program
                )
                runProgram(current)
                if (current.output == current.program) {
                    println("done $aRegister")
                    return@let aRegister
                }

//                // 3 7 4 6 3 2 1 0 7 7 7 0 3 3 1 0 3 7 6 2 3 3 1 0 7 7 1 4 2 01 0 3 7 0 6 2 0 1
//                row {
//                    cells(
//                        aRegister,
//                        Integer.toBinaryString(aRegister.toInt()),
//                        Integer.toBinaryString(current.regA.toInt()),
//                        Integer.toBinaryString(current.regB.toInt()),
//                        Integer.toBinaryString(current.regC.toInt()),
//                        current.output.map { Integer.toBinaryString(it.toInt()) }.joinToString(","),
//                        "",
//                        current.regA,
//                        current.regB,
//                        current.regC,
//                        current.output.joinToString(","),
//                        current.output.map { Integer.toBinaryString(it.toInt()) }.joinToString("").let { Integer.valueOf(it, 2) }
//                    ) {
//                        this.alignment = TextAlignment.MiddleRight
//                    }
//                }

                aRegister++
            }
//        }.also {
//            println(it)
//        }

        aRegister
//        throw Exception("huh")
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
        output.add(combo(operand) % 8L)
    }

    fun Parser.Input.bdv(operand: Long) {
        regB = division(operand)
    }

    fun Parser.Input.cdv(operand: Long) {
        regC = division(operand)
    }

    private fun Parser.Input.combo(op: Long): Long = when (op) {
        in (0..3) -> op.toLong()
        4L -> regA
        5L -> regB
        6L -> regC
        else -> throw Exception("Found operand $op")
    }
}
