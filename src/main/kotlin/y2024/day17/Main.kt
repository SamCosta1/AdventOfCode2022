package y2024.day17

import kotlinx.coroutines.*
import kotlinx.coroutines.selects.select
import puzzlerunners.Puzzle
import utils.RunMode
import kotlin.math.pow

typealias NewInstructionIndex = Long

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

    private fun runProgramP2(info: Parser.Input): Boolean {
        var programIndex = 0
        var outputIndex = 0
        val aOrig = info.regA

        while (programIndex <= info.program.lastIndex) {
            val operation = info.program[programIndex]
            val operand = info.program[programIndex + 1]

            when (operation) {
                0L -> info.adv(operand)
                1L -> info.bxl(operand)
                2L -> info.bst(operand)
                3L -> {
                    info.jnz(operand)?.let {
                        programIndex = it - 2
                    }
                }

                4L -> info.bxc(operand)
                5L -> {
                    val out = info.combo(operand) % 8
                    if (outputIndex > info.program.lastIndex || info.program[outputIndex] != out) {
                        return false
                    }
                    outputIndex++
                }

                6L -> info.bdv(operand)
                7L -> info.cdv(operand)
            }

            programIndex += 2
        }

        return outputIndex == info.program.size
    }


    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        if (runMode == RunMode.Sample) {
            return@let part2ExpectedAnswerForSample
        }
        runBlocking {
            println(info)
            val aStart = 2.0.pow((info.program.size - 1) * 3).toLong()
            val maxAReg = 2.0.pow((info.program.size) * 3).toLong()

            val numToCheck = (maxAReg - aStart)
            println("Num to check = $numToCheck")
            val threads = Runtime.getRuntime().availableProcessors()
            val inc = numToCheck / threads
            println("aStart $aStart max $maxAReg threads $threads inc $inc")

            val scope = CoroutineScope(Dispatchers.Default)
            return@runBlocking select<Long> {
                repeat(threads) { thread ->
                    scope.async {
                        println("Task $thread is running on thread: ${Thread.currentThread().name}")
                        println("$runMode Starting with $thread ${aStart + thread * inc}")
                        runSubset(aStart + thread * inc, info)
                    }.onAwait {
                        scope.cancel()
                        it
                    }
                }
            }
        }
    }

    private fun runSubset(
        aStart: Long,
        info: Parser.Input
    ): Long {
        var aRegister = aStart
        while (true) {
            val current = Parser.Input(
                regA = aRegister,
                regB = info.regB,
                regC = info.regC,
                output = null,
                program = info.program
            )

            if (runProgramP2(current)) {
                println("done $aRegister")
                return aRegister
            }

            aRegister++
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
}
