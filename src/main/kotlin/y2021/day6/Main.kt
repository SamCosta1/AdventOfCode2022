package y2021.day6

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any,
    override val isComplete: Boolean = true
) : Puzzle {

    private fun runSimulation(totalDays: Int, fishes: List<Int>): Long {
        return fishes.sumOf { dayOfFirstChild ->
            runIndividualSimulation(dayOfFirstChild, totalDays)
        } + fishes.size.toLong()
    }

    private val cache = mutableMapOf<Int, Long>()
    private fun runIndividualSimulation(dayOfFirstChild: Int, totalDays: Int): Long {
        if (dayOfFirstChild >= totalDays) {
            return 0
        }

        if (cache.containsKey(dayOfFirstChild)) {
            return cache[dayOfFirstChild]!!
        }

        val childrenProduced = 1 + (totalDays - dayOfFirstChild - 1) / 7

        val total = (0 until childrenProduced).sumOf { childIndex ->
            runIndividualSimulation(9 + dayOfFirstChild + 7 * childIndex, totalDays)
        } + childrenProduced
        cache[dayOfFirstChild] = total

        return total
    }

    private fun runNaiive(days: Int, values: List<Long>): Any {
        val currentState = values.toMutableList()

        repeat(days) {
            var numNew = 0
            currentState.forEachIndexed { index, state ->
                currentState[index] = if (state == 0L) {
                    numNew++
                    6L
                } else {
                    state - 1L
                }
            }

            (0 until numNew).map { 8L }.forEach { currentState.add(it) }
        }

        return currentState.count()

    }

    override fun runPart1(data: List<String>, runMode: RunMode) =
        runSimulation(80, data.first().split(",").map { it.toInt() })

    override fun runPart2(data: List<String>, runMode: RunMode) =
        runSimulation(256, data.first().split(",").map { it.toInt() })
}
