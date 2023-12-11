package y2022.day5

import java.nio.file.Files
import java.nio.file.Paths

object Parser {
    data class Instruction(val sourceIndex: Int, val targetIndex: Int, val count: Int)

    data class Info(val instructions: List<Instruction>, val stacks: List<ArrayDeque<Char>>)

    private val instructionRegex = "move (\\d+) from (\\d+) to (\\d)".toRegex()
    fun parse(data: List<String>): Info {
        val dividerIndex = data.indexOfFirst { it.isBlank() }
        val instructions = data.drop(dividerIndex + 1).map {
            val values =
                instructionRegex.matchEntire(it)?.groupValues?.drop(1)?.map { rawLine -> rawLine.toInt() }!!
            Instruction(count = values.first(), sourceIndex = values[1] - 1, targetIndex = values[2] - 1)
        }

        val stacks = data.take(dividerIndex - 1).let { rawList ->
            val stacks = (1..rawList.maxBy { it.length }.chunked(4).size).map { ArrayDeque<Char>() }

            rawList.reversed()
                .map { rawRaw ->
                    val row = rawRaw.chunked(4).map { raw -> raw.toCharArray().firstOrNull { it.isLetter() } }

                    row.forEachIndexed { index, c ->
                        if (c != null) {
                            stacks[index].add(c)
                        }
                    }
                    row
                }
            stacks
        }

        return Info(instructions, stacks)
    }
}