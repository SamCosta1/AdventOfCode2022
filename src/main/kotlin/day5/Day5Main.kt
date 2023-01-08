package day5

import java.nio.file.Files
import java.nio.file.Paths

class Day5Main {

    data class Instruction(val sourceIndex: Int, val targetIndex: Int, val count: Int)

    private val instructionRegex = "move (\\d+) from (\\d+) to (\\d)".toRegex()
    val instructions =
        Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/day5/data.txt")).map {

            val values = instructionRegex.matchEntire(it)?.groupValues?.drop(1)?.map { rawLine -> rawLine.toInt() }!!
            Instruction(count = values.first(), sourceIndex = values[1] - 1, targetIndex = values[2] - 1)
        }

    val stacks = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/day5/stacks.txt"))
        ?.let { rawList ->
            val stacks = (1..rawList.first().chunked(4).size).map { ArrayDeque<Char>() }

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
        }!!

    fun run(): String {
        instructions.forEach { instruction ->
            repeat(instruction.count) {
                val popped = stacks[instruction.sourceIndex].removeLast()
                stacks[instruction.targetIndex].addLast(popped)
            }
        }
        return stacks.map { it.last() }.joinToString("")
    }

    fun runPart2(): String {
        instructions.forEach { instruction ->
            val toBeAdded = mutableListOf<Char>()
            repeat(instruction.count) {
                val popped = stacks[instruction.sourceIndex].removeLast()
                toBeAdded.add(popped)
            }

            toBeAdded.reversed().forEach {
                stacks[instruction.targetIndex].addLast(it)
            }
        }
        return stacks.map { it.last() }.joinToString("")
    }
}