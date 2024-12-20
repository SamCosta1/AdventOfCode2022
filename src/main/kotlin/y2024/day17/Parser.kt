package y2024.day17

import utils.split

object Parser {
    data class Input(var regA: Long, var regB: Long, var regC: Long, val output: MutableList<Long>, val program: List<Long>)
    fun parse(data: List<String>): Input {
        val (registers, program) = data.split { it.isBlank() }

        return Input(
            registers[0].split(": ").last().toLong(),
            registers[1].split(": ").last().toLong(),
            registers[2].split(": ").last().toLong(),
            mutableListOf(),
            program.first().split(": ").last().split(",").map { it.toLong() }
        )
    }
}