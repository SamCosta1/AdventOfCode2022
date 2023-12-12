package y2023.day12

import kotlinx.coroutines.*
import org.apache.commons.math3.util.CombinatoricsUtils
import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.RunMode
import java.util.ArrayList
import kotlin.math.min

class Main(
    override val part1ExpectedAnswerForSample: Any = 21,
    override val part2ExpectedAnswerForSample: Any = 525152L,
    override val isComplete: Boolean = false
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).takeLast(1).sumOf { countArrangements(it) }

    private fun countArrangements(log: Parser.LogEntry): Long {
        if (log.springs.all { it == Parser.SpringState.Unknown }) {
            return allUnknown(log.springs, log.damagedGroups)
        }
        return go(log.springs.joinToString(""), 0, log.springs.toMutableList(), log.damagedGroups)
    }

    private fun go(
        id: String,
        startIndex: Int,
        springs: MutableList<Parser.SpringState>,
        targetGroup: List<Int>
    ): Long {
        if (!targetGroup.startsWith(springs.groups())) {
            return 0
        }

        if (springs.none { it == Parser.SpringState.Unknown }) {
            return if (targetGroup == springs.groups()) 1L.also {
//                println(springs.joinToString(""))
            } else 0L
        }

        return if (springs[startIndex] == Parser.SpringState.Unknown) {
            go(id, startIndex + 1, springs.also {
                it[startIndex] = Parser.SpringState.Broken
            }, targetGroup) + go(id, startIndex + 1, springs.toMutableList().also {
                it[startIndex] = Parser.SpringState.Working
            }, targetGroup).also {
                springs[startIndex] = Parser.SpringState.Unknown
            }
        } else {
            go(id, startIndex + 1, springs, targetGroup)
        }
    }

    private fun <T> List<T>.startsWith(start: List<T>): Boolean {
        if (start.size > size) {
//            println("size $start $this")
            return false
        }

        for ((index, item) in start.withIndex()) {
            if (item != this[index]) {
//                println("return false $start $this")
                return false
            }
        }
        return true
    }

    private fun List<Parser.SpringState>.groups(): List<Int> {
        val groups = mutableListOf<Int>()
        var currentGroupSize = 0
        var brokeBecauseUnknown = false
        for (it in this) {
            if (it == Parser.SpringState.Unknown) {
                brokeBecauseUnknown = true
                break
            }

            if (it == Parser.SpringState.Working) {
                if (currentGroupSize != 0) {
                    groups.add(currentGroupSize)
                }
                currentGroupSize = 0
            } else {
                currentGroupSize++
            }
        }
        if (!brokeBecauseUnknown && currentGroupSize != 0) {
            groups.add(currentGroupSize)
        }

        return groups
    }

    private fun allUnknown(
        springs: List<Parser.SpringState>,
        targetGroup: List<Int>
    ): Long {
        val remainingUnknowns = springs.size - targetGroup.sum()
        val mustBeDots = targetGroup.size - 1
        val possibleSpaces =targetGroup.size + 1
        val freeDots = remainingUnknowns - mustBeDots

//        println("$remainingUnknowns $mustBeDots $possibleSpaces $freeDots")

        return CombinatoricsUtils.binomialCoefficient(freeDots + possibleSpaces - 1, possibleSpaces -1)
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Long {
//        if (runMode != RunMode.Sample) {
//            return 0L
//        }
        val parsed = Parser.parsePart2(data)
        println(parsed.size)
        var count = 0
        return parsed.sumOf {
            countArrangements(it).also {

                    println("Count ${count++}")

            }
        }
    }
}
