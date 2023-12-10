package y2022.day5

import puzzlerunners.Puzzle
import utils.RunMode
import java.nio.file.Files
import java.nio.file.Paths

class Main(
    override val part1ExpectedAnswerForSample: Any = 0,
    override val part2ExpectedAnswerForSample: Any = 0,
    override val isComplete: Boolean = true
): Puzzle {

    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (instructions, stacks) ->
        instructions.forEach { instruction ->
            repeat(instruction.count) {
                val popped = stacks[instruction.sourceIndex].removeLast()
                stacks[instruction.targetIndex].addLast(popped)
            }
        }
        stacks.map { it.last() }.joinToString("")
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (instructions, stacks) ->
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
        stacks.map { it.last() }.joinToString("")
    }
}