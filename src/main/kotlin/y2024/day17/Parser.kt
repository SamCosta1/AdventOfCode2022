package y2024.day17

import utils.split

object Parser {
    data class Input(var regA: Int, var regB: Int, var regC: Int, val output: MutableList<Int>, val program: List<Int>)
    fun parse(data: List<String>): Input {
        val (registers, program) = data.split { it.isBlank() }

        return Input(
            registers[0].split(": ").last().toInt(),
            registers[1].split(": ").last().toInt(),
            registers[2].split(": ").last().toInt(),
            mutableListOf(),
            program.first().split(": ").last().split(",").map { it.toInt() }
        )
    }
}