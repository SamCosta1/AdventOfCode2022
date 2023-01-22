package y2021.day6

import utils.Puzzle
import utils.RunMode

class Main: Puzzle {

    private fun runSimulation(days: Int, data: List<String>) = data.first().split(",").map { it.toLong() }.let { values ->
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

            println("Day ${it + 1} ${currentState.size}")
        }

        currentState.count()
    }

    override fun runPart1(data: List<String>, runMode: RunMode) = runSimulation(80, data)
    override fun runPart2(data: List<String>, runMode: RunMode) = runSimulation(256, data)
}
