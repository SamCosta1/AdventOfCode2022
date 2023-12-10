package y2021.day14

import puzzlerunners.Puzzle
import utils.RunMode

typealias SubResult = Map<Char, Long>
class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = runMoreIntelligent(Parser.parse(data), repeats = 10)
    override fun runPart2(data: List<String>, runMode: RunMode) = runMoreIntelligent(Parser.parse(data), repeats = 40)

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
        }

        val counts = currentState.toSet().map { char ->
            currentState.count { char == it }
        }
        return counts.max()!! - counts.min()!!
    }

    private fun runMoreIntelligent(data: Parser.Data, repeats: Int): Long {
        val startingPairs = mutableListOf<String>()
        (1 until data.template.length).forEach { index ->
            startingPairs.add("${data.template[index-1]}${data.template[index]}")
        }

        val finalResult = mutableMapOf<Char, Long>()
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ".forEach { finalResult[it] = 0 }

        data.template.forEach {
            finalResult[it] = finalResult[it]!! + 1L
        }

        startingPairs.map { startPair(it, repeats, data.mappings) }.forEach { subResult ->
            subResult.forEach { char, count ->
                finalResult.put(char, finalResult.get(char)!! + count)
            }
        }
        return finalResult.values.max()!! - finalResult.values.filter { it != 0L }.min()!!
    }

    val overallCache: MutableMap<String, MutableMap<Int, SubResult>> = mutableMapOf()
    private fun startPair(startPair: String, iterations: Int, mappings: Map<String, String>): SubResult {
        overallCache[startPair]?.get(iterations)?.let {
            return it
        }

        if (iterations == 0) {
            return emptyMap()
        }

        val result = mutableMapOf<Char, Long>()

        val results = getResultingPairsAndNewChars(startPair, mappings)
        if (results.newChar != null) {
            result[results.newChar] = result.getOrDefault(results.newChar, 0) + 1
        }

        results.resultingPairs.forEach { pair ->
            startPair(pair, iterations - 1, mappings).forEach { (char, number) ->
                result[char] = result.getOrDefault(char, 0) + number
            }
        }
        return result.also {
            overallCache.getOrPut(startPair) { mutableMapOf() }[iterations] = result
        }
    }

    data class IntermitantResult(val resultingPairs: List<String>, val newChar: Char?)

    val cache = mutableMapOf<String, IntermitantResult>()
    fun getResultingPairsAndNewChars(startPair: String, mappings: Map<String, String>): IntermitantResult {
        cache[startPair]?.let { return it }

        val newChar = mappings[startPair] ?: return IntermitantResult(emptyList(), null).also { cache[startPair] = it }

        return IntermitantResult(listOf(startPair[0] + newChar, newChar + startPair[1]), newChar[0]).also { cache[startPair] = it }
    }

}