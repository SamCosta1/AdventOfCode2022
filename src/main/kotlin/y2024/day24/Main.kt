package y2024.day24

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 2024L,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
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
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
