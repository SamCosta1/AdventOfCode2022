package y2024.day19

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 6,
    override val part2ExpectedAnswerForSample: Any = 16,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (towels, combos) ->
        cache.clear()
        println("$runMode ${towels.size}")
        combos.count { combo ->
            val number = numberOfSols(combo, towels).size
            println("Combo $combo | $number")
            number > 0
        }.also { println(it) }
    }

    val cache = mutableMapOf<String, List<Set<String>>>()

    // 208 too low
    private fun numberOfSols(combo: String, towels: Set<String>): List<Set<String>> {
        if (cache[combo] != null) {
            return cache[combo]!!
        }

        if (combo.length <= 1 && !towels.contains(combo)) {
            return emptyList()
        }
        var count = mutableListOf<Set<String>>()
        if (towels.contains(combo)) {
            count += setOf(towels)
        }
        combo.dropLast(1).forEachIndexed { index, c ->
            val firstHalf = numberOfSols(combo.take(index + 1), towels)
            val secondHalf = numberOfSols(combo.drop(index + 1), towels)
            if (firstHalf.isNotEmpty() && secondHalf.isNotEmpty()) {
                firstHalf.forEach { first ->
                    secondHalf.forEach { second ->
                        count += first.union(second)
                    }
                }
            }
        }
        cache[combo] = count
        return count
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (towels, combos) ->
        cache.clear()
        combos.sumOf { combo ->
            val number = numberOfSols(combo, towels).size
            println("Combo $combo | $number")
            number
        }.also { println(it) }
    }
}
