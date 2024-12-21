package y2024.day19

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 6,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (towels, combos) ->
        cache.clear()

        println("no towels ${towels.size}")
        combos.forEach { combo ->
            println(towels.filter { combo.contains(it) }.size)
        }
        combos.count { combo ->
            val number = numberOfSols(combo, towels)
            println("Number fo sols $combo $number")
            number > 0
        }.also { println(it) }
    }

    val cache = mutableMapOf<String, Long>()

    private fun numberOfSols(combo: String, towels: Set<String>): Long {
        if (cache[combo] != null) {
            return cache[combo]!!
        }

        if (combo.length <= 1 && !towels.contains(combo)) {
            return 0
        }
        var count = if (towels.contains(combo)) 1L else 0L
        combo.dropLast(1).forEachIndexed { index, c ->
            val firstHalf = numberOfSols(combo.take(index + 1), towels)
            if (firstHalf > 0L) {
                val secondHalf = numberOfSols(combo.drop(index + 1), towels)
                 count += firstHalf.coerceAtMost(1) * secondHalf.coerceAtMost(1)
            }
        }
        cache[combo] = count
        return count
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
