package y2023.day8

import utils.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        val endNode = info.nodes["ZZZ"]!!
        var currentNode = info.nodes["AAA"]!!
        var count = 0L

        while (currentNode != endNode) {
            val instruction = info.instructions[(count % info.instructions.size).toInt()]
            currentNode = currentNode.linkFor(instruction)
            count++
        }

        count
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
