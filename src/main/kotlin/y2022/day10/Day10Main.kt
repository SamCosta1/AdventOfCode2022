package y2022.day10

import java.nio.file.Files
import java.nio.file.Paths

object Day10Main {

    sealed class Instruction {
        object Noop : Instruction()
        data class Add(val num: Int) : Instruction()
    }

    val data =
        Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day10/data.txt")).map {
            if (it == "noop") {
                Instruction.Noop
            } else {
                Instruction.Add(it.split(" ").last().toInt())
            }
        }

    data class State(val cycleNum: Int, val x: Int) {
        val strength: Int get() = cycleNum * x
    }

    private fun execute(): List<State> {
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

    fun run() = execute().filter { (it.cycleNum - 20) % 40 == 0 }.sumBy { it.strength }.toString()

    fun runPart2(): String {
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


            if ((cycle-1) % 40 == 0) {
                str.appendLine()
            }
        }

        return str.toString()
    }
}