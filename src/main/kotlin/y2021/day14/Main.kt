package y2021.day14

import utils.Puzzle
import utils.RunMode

class Main : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { data ->
        runSimulation(data, repeats = 10)
    }

    private fun runSimulation(data: Parser.Data, repeats: Int): Int {
        var currentState = data.template

        repeat(repeats) { step ->
            var index = 1
            currentState = buildString {
                while (index < currentState.length) {
                    val thisPair = "${currentState[index - 1]}${currentState[index]}"
                    val mapping = data.mappings.get(thisPair) ?: ""
                    append(currentState[index - 1])
                    append(mapping)
                    index++
                }
                append(currentState.last())
            }
            println("Step complete $step ${currentState.length}")
        }

        val counts = currentState.toSet().map { char ->
            currentState.count { char == it }
        }
        return counts.max()!! - counts.min()!!
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { data ->
        runSimulation(data, repeats = 40)
    }
}