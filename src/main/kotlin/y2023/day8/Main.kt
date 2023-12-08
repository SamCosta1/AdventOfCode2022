package y2023.day8

import utils.Puzzle
import utils.RunMode
import y2022.day19.productOf
import y2022.day19.productOfLong

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
) : Puzzle {
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

    override fun runPart2(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { info ->
        val startNodes = info.nodes.filter { it.key.endsWith("A") }

        val steps = arrayOfNulls<Long>(startNodes.size)

        var currentNodes = startNodes.values
        var count = 0L

        while (steps.any { it == null }) {
            val instruction = info.instructions[(count % info.instructions.size).toInt()]
            currentNodes = currentNodes.map { it.linkFor(instruction) }
            count++
            currentNodes.forEachIndexed { index, node ->
                if (node.label.endsWith("Z") && steps[index] == null) {
                    steps[index] = count
                }
            }
        }

        findLCMOfListOfNumbers(steps.map { it!! })
    }
}

fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
    var result = numbers[0]
    for (i in 1 until numbers.size) {
        result = findLCM(result, numbers[i])
    }
    return result
}

fun findLCM(a: Long, b: Long): Long {
    val larger = if (a > b) a else b
    val maxLcm = a * b
    var lcm = larger
    while (lcm <= maxLcm) {
        if (lcm % a == 0L && lcm % b == 0L) {
            return lcm
        }
        lcm += larger
    }
    return maxLcm
}
