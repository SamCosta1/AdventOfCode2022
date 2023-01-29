package y2021.day14

import utils.Puzzle
import utils.RunMode

class Main : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { data ->
        var currentState = data.template

        println(currentState)
        repeat(10) { step ->
            var index = 1
            currentState = buildString {
                while (index < currentState.length) {
                    val thisPair = "${currentState[index-1]}${currentState[index]}"
                    val mapping = data.mappings.firstOrNull { it.source == thisPair }?.dest ?: ""
                    append(currentState[index-1])
                    append(mapping)
                    index++
                }
                append(currentState.last())
            }
        }

        val counts = currentState.toSet().map { char -> currentState.count { char == it } }
        counts.max()!! - counts.min()!!
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
