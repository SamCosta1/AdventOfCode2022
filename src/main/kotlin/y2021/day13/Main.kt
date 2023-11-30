package y2021.day13

import utils.Point
import utils.Puzzle
import utils.RunMode

class Main(override val part1ExpectedAnswerForSample: Any, override val part2ExpectedAnswerForSample: Any) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { parsed ->
        performInstructions(parsed.points, parsed.instructions.take(1)).size
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { parsed ->
        Parser.pointsToString(performInstructions(parsed.points, parsed.instructions), null)
    }

    private fun performInstructions(initialData: List<Point>, instructions: List<Parser.Instruction>): Set<Point> {
        var points = initialData.toSet()

        instructions.forEach { instruction ->
            points = performInstruction(points, instruction)

        }

        return points
    }

    private fun performInstruction(points: Set<Point>, instruction: Parser.Instruction): Set<Point> {
        return if (instruction.axis == "y") {
            val unchanged = points.filter { it.y < instruction.line }
            val altered = points.filter { it.y > instruction.line }.map {
                it.copy(y = instruction.line - (it.y - instruction.line))
            }
            (unchanged + altered).toSet()
        } else {
            val unchanged = points.filter { it.x < instruction.line }
            val altered = points.filter { it.x > instruction.line }.map {
                it.copy(x = instruction.line - (it.x - instruction.line))
            }
            (unchanged + altered).toSet()
        }
    }

}
