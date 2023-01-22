package y2021.day6

import utils.Puzzle
import utils.RunMode
import utils.debug

class Main : Puzzle {

    var totalFish: Long = 0L
    private fun runSimulation(totalDays: Int, fishes: List<Int>): Long {
        fishes.forEach { dayOfFirstChild ->
            runIndividualSimulation(dayOfFirstChild, totalDays)
        }

        totalFish += fishes.size
        return totalFish
    }

    private fun runIndividualSimulation(dayOfFirstChild: Int, totalDays: Int) {
        if (dayOfFirstChild >= totalDays) {
            return
        }

        val childrenProduced = 1 + (totalDays - dayOfFirstChild - 1) / 7
//        println("Fish which starts at ${dayOfFirstChild.debug()} produced $childrenProduced")
        totalFish += childrenProduced

        repeat(childrenProduced) { childIndex ->
            runIndividualSimulation(9 + dayOfFirstChild + 7 * childIndex, totalDays)
        }
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
//            println("After day ${it+1}  $currentState")
        }

        return currentState.count()

    }

    override fun runPart1(data: List<String>, runMode: RunMode) =
        runSimulation(80, data.first().split(",").map { it.toInt() })

    override fun runPart2(data: List<String>, runMode: RunMode) =
        runSimulation(256, data.first().split(",").map { it.toInt() })
}
