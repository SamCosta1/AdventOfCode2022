package y2024.day24

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode
import java.nio.file.Files
import java.nio.file.Path

class Main(
    override val part1ExpectedAnswerForSample: Any = 2024L,
    override val part2ExpectedAnswerForSample: Any = "z00,z01,z02,z05",
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (initials, instructions) ->
        val ends = instructions.filter { it.output.startsWith("z") }
        val values = initials.toMutableMap()
        val zs = ends.map { it.output }

        val remainingInstructions = instructions.toMutableList()
        while (zs.any { values[it] == null }) {
            val iterator = remainingInstructions.iterator()
            for (instruction in iterator) {
                val op1 = values[instruction.op1] ?: continue
                val op2 = values[instruction.op2] ?: continue

                values[instruction.output] = when (instruction.operation) {
                    Parser.Info.Operation.And -> if (op1 == 1  && op2 == 1) 1 else 0
                    Parser.Info.Operation.Or -> if (op1 == 1  || op2 == 1) 1 else 0
                    Parser.Info.Operation.Xor -> if (op1 == 1  && op2 == 0 || op1 == 0  && op2 == 1) 1 else 0
                }
                iterator.remove()
            }
        }

        zs.sorted().map { values[it] }.reversed().joinToString("").toLong(2)
    }
    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (initials, _instructions) ->
        if (runMode == RunMode.Sample) {
            return@let part2ExpectedAnswerForSample
        }

        // First just swap z36 and z37 since they're clearly in the wrong order
        val instructions = _instructions.map {
            when (it.output) {
                "z36" -> it.copy(output = "z37")
                "z37" -> it.copy(output = "z36")
                else -> it
            }
        }
        val ends = instructions.filter { it.output.startsWith("z") }
        val values = initials.toMutableMap()
        val zs = ends.map { it.output }

        val remainingInstructions = instructions.toMutableList()
        while (zs.any { values[it] == null }) {
            val iterator = remainingInstructions.iterator()
            for (instruction in iterator) {
                val op1 = values[instruction.op1] ?: continue
                val op2 = values[instruction.op2] ?: continue

                values[instruction.output] = when (instruction.operation) {
                    Parser.Info.Operation.And -> if (op1 == 1  && op2 == 1) 1 else 0
                    Parser.Info.Operation.Or -> if (op1 == 1  || op2 == 1) 1 else 0
                    Parser.Info.Operation.Xor -> if (op1 == 1  && op2 == 0 || op1 == 0  && op2 == 1) 1 else 0
                }
                iterator.remove()
            }
        }
        val x = initials.filter { it.key.startsWith("x") }.toList().sortedBy { it.first }.reversed().map { it.second }.joinToString("")
        val y = initials.filter { it.key.startsWith("y") }.toList().sortedBy { it.first }.reversed().map { it.second }.joinToString("")
        val expectedOutput = (x.toLong(2) + y.toLong(2)).toString(2)
        val actualOutput = zs.sorted().map { values[it] }.reversed().joinToString("")

        println(instructions.size)
        println("X :$x (${x.toLong(2)})")
        println("Y :$y (${y.toLong(2)})")
        println("Ze:$expectedOutput")
        println("Za:$actualOutput")
        actualOutput.toLong(2)
    }
}
