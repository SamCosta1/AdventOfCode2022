package y2021.day2

import utils.Puzzle
import utils.RunMode
import java.lang.Exception

private sealed class Instruction {
    data class Up(val count: Int): Instruction()
    data class Down(val count: Int): Instruction()
    data class Forward(val count: Int): Instruction()

    companion object {
        fun from(raw: String): Instruction {
            val split = raw.split(" ")
            return when (split[0]) {
                "forward" -> Forward(split[1].toInt())
                "up" -> Up(split[1].toInt())
                "down" -> Down(split[1].toInt())
                else -> throw Exception()
            }
        }
    }
}

class Main(override val part1ExpectedAnswerForSample: Any, override val part2ExpectedAnswerForSample: Any) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.map { Instruction.from(it) }.let { instructions ->
        var horizontal = 0
        var depth = 0
        instructions.forEach {
            when (it) {
                is Instruction.Down -> depth += it.count
                is Instruction.Forward -> horizontal += it.count
                is Instruction.Up -> depth -= it.count
            }
        }

        horizontal * depth
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = data.map { Instruction.from(it) }.let { instructions ->
        var aim = 0L
        var horizontal = 0L
        var depth = 0L
        instructions.forEach {
            when (it) {
                is Instruction.Down -> aim += it.count
                is Instruction.Forward ->  {
                    horizontal += it.count
                    depth += aim * it.count
                }
                is Instruction.Up -> aim -= it.count
            }
        }

        horizontal * depth
    }
}
