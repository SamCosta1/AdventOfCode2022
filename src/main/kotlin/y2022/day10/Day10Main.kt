package y2022.day10

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 0,
    override val part2ExpectedAnswerForSample: Any = 0,
    // No expected answers here since they need printing to console to make sense
    override val isComplete: Boolean = true
): Puzzle{

    sealed class Instruction {
        object Noop : Instruction()
        data class Add(val num: Int) : Instruction()
    }

    data class State(val cycleNum: Int, val x: Int) {
        val strength: Int get() = cycleNum * x
    }

    private fun execute(data: List<Instruction>): List<State> {
        var x = 1
        var cycle = 1

        val states = mutableListOf<State>()
        var instructionIndex = 0
        var hasStartedInstruction = false

        while (instructionIndex < data.size) {

            when (val instruction = data[instructionIndex]) {
                Instruction.Noop -> {
                    states.add(State(cycle++, x))
                    instructionIndex++
                }
                is Instruction.Add -> {
                    if (hasStartedInstruction) {
                        states.add(State(cycle++, x))
                        x += instruction.num
                        hasStartedInstruction = false
                        instructionIndex++
                    } else {
                        states.add(State(cycle++, x))
                        hasStartedInstruction = true
                    }
                }
            }
        }

        return states
    }

    private fun parse(data: List<String>) = data.map {
        if (it == "noop") {
            Instruction.Noop
        } else {
            Instruction.Add(it.split(" ").last().toInt())
        }
    }

    override fun runPart1(data: List<String>, runMode: RunMode) = execute(parse(data))
        .filter { (it.cycleNum - 20) % 40 == 0 }
        .sumBy { it.strength }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = parse(data).let { instructions ->
        var x = 1
        var cycle = 1

        val states = mutableListOf<State>()
        var instructionIndex = 0
        var hasStartedInstruction = false

        var str = StringBuilder()
        while (instructionIndex < data.size) {

            if ((cycle-1) % 40 in listOf(x-1, x, x+1)) {
                str.append("██")
            } else {
                str.append("  ")
            }

            when (val instruction = instructions[instructionIndex]) {
                Instruction.Noop -> {
                    states.add(State(cycle++, x))
                    instructionIndex++
                }
                is Instruction.Add -> {
                    if (hasStartedInstruction) {
                        states.add(State(cycle++, x))
                        x += instruction.num
                        hasStartedInstruction = false
                        instructionIndex++
                    } else {
                        states.add(State(cycle++, x))
                        hasStartedInstruction = true
                    }
                }
            }


            if ((cycle-1) % 40 == 0) {
                str.appendLine()
            }
        }

        str.toString()
    }
}
