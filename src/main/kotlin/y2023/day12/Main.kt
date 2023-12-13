package y2023.day12

import org.apache.commons.math3.util.CombinatoricsUtils
import puzzlerunners.Puzzle
import utils.RunMode
import utils.safeSubList

class Main(
    override val part1ExpectedAnswerForSample: Any = 21,
    override val part2ExpectedAnswerForSample: Any = 525152L,
    override val isComplete: Boolean = false
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Long {
//        println(
//            "Checking ${
//                allNotDefoBroken(
//                    "id",
//                    listOf(
//                        Parser.SpringState.Working,
//                        Parser.SpringState.Working,
//                        Parser.SpringState.Broken,
//                        Parser.SpringState.Broken,
//                        Parser.SpringState.Broken,
//                        Parser.SpringState.Working,
//                        Parser.SpringState.Working
//                    ),
//                    listOf(1)
//                )
//            }"
//        )
//                println(
//            "Checking ${
//                allNotDefoBroken(
//                    "id",
//                    listOf(
//                        Parser.SpringState.Working,
//                        Parser.SpringState.Unknown,
//                        Parser.SpringState.Working,
//                        Parser.SpringState.Unknown,
//                        Parser.SpringState.Unknown,
//                        Parser.SpringState.Working
//                    ),
//                    listOf(1,1)
//                )
//            }"
//        )

        return 0L
//        return Parser.parse(data).sumOf { entry ->
//            countArrangements(entry).also {
////                println("$it | ${entry.springs.joinToString("")}")
//            }
//        }
    }

    private fun countArrangements(log: Parser.LogEntry): Long {
        if (log.springs.all { it == Parser.SpringState.Unknown }) {
            return allUnknown(log.springs.size, log.damagedGroups)
        }
        return go2(log.springs.joinToString(""), log.springs.toMutableList(), log.damagedGroups)
//        return go(log.springs.joinToString(""), 0, log.springs.toMutableList(), log.damagedGroups)
    }

    private fun countArrangementsOld(log: Parser.LogEntry): Long {
//        if (log.springs.all { it == Parser.SpringState.Unknown }) {
//            return allUnknown(log.springs.size, log.damagedGroups)
//        }
        return go(log.springs.joinToString(""), 0, log.springs.toMutableList(), log.damagedGroups)
    }

    // 506250
    private fun go2(
        id: String,
        springs: List<Parser.SpringState>,
        targetGroup: List<Int>
    ): Long {

        val indexOfFirstHash = springs.indexOfFirst { it == Parser.SpringState.Broken }
//        // println("$id springs ${springs.joinToString("")}")

        if (indexOfFirstHash < 0) {
            return allNotDefoBroken(id, springs, targetGroup).also {
                // // println("indexOfFirstHash < 0 = $it")
            }
        }
        var total = 0L

        for ((groupIndex, brokeInGroup) in targetGroup.withIndex()) {
            // Trying to see what happens if we assume that the first # is in this group

            val safeSubListStartingHere = springs.safeSubList(indexOfFirstHash, springs.size)
            val existingHashes = safeSubListStartingHere.countWhile { it == Parser.SpringState.Broken }
            val hashesToAdd = brokeInGroup - existingHashes

            if (hashesToAdd < 0) {
                continue
            }

            val possibleStartIndexes = mutableListOf<Int>()
            repeat(brokeInGroup) { indexOfFirstHashInGroup ->
                val groupStartListIndex = indexOfFirstHash - indexOfFirstHashInGroup

                // // println("brokeInGroup $brokeInGroup indexOfFirstHashInGroup $indexOfFirstHashInGroup groupStartListIndex $groupStartListIndex")
                if (
                    groupStartListIndex >= 0
                    && groupStartListIndex + brokeInGroup <= springs.size
                    && springs.safeSubList(groupStartListIndex, groupStartListIndex + brokeInGroup).countWhile {
                        it != Parser.SpringState.Working
                    } >= brokeInGroup
                    && ((springs.getOrNull(groupStartListIndex + brokeInGroup)
                        ?: Parser.SpringState.Working) != Parser.SpringState.Broken)
                ) {
                    possibleStartIndexes += groupStartListIndex
                }
            }

            // // println("broke $brokeInGroup possible start indexes  $possibleStartIndexes")

            for (index in possibleStartIndexes) {
                // // println("startindex $index ")

                val a = allNotDefoBroken(
                    id,
                    springs.safeSubList(0, index - 1),
                    targetGroup.safeSubList(0, groupIndex)
                )
                val b = go2(
                    id + " recurse",
                    springs.safeSubList(index + brokeInGroup + 1, springs.size),
                    targetGroup.safeSubList(groupIndex + 1, targetGroup.size)
                )
//                println("Got AB ${springs.joinToString("")} a=$a b=$b")
                total += a * b
            }

        }

        return total
    }


    fun <T> List<T>.countWhile(predicate: (T) -> Boolean): Int {
        var count = 0
        for (x in this) {
            if (predicate(x)) {
                count++
            } else {
                return count
            }
        }
        return count
    }

    private fun allNotDefoBroken(
        id: String,
        springs: List<Parser.SpringState>,
        targetGroup: List<Int>
    ): Long {
        val cleanedUpSprings =
            listOf(Parser.SpringState.Working) + springs.removeSurrounding(Parser.SpringState.Working) + listOf(
                Parser.SpringState.Working
            )
        val indexesOfDots = cleanedUpSprings.mapIndexedNotNull { index, springState ->
            index.takeIf { springState == Parser.SpringState.Working }
        }
        val unknownGroupSizes =
            (if (indexesOfDots.isEmpty()) listOf(cleanedUpSprings.size) else indexesOfDots.mapIndexed { index, dotIndex ->
                if (index == 0) {
                    dotIndex
                } else {
                    dotIndex - indexesOfDots[index - 1] - 1
                }
            }).filter { it > 0 }

        return allNotDefoBroken2(id, unknownGroupSizes, targetGroup).also {
//            println("allNotDefoBroken Total $it | for ${springs.joinToString("")} cleanedup=${cleanedUpSprings} groups=$unknownGroupSizes indexesOfDots=$indexesOfDots target=$targetGroup")
        }
    }

    private fun allNotDefoBroken2(
        id: String,
        unknownGroupSizes: List<Int>,
        targetGroup: List<Int>
    ): Long {
//          // println("allNotDefoBroken2 sizes=$unknownGroupSizes target=$targetGroup")

        if (targetGroup.isNotEmpty() && unknownGroupSizes.isEmpty()) {
            return 0L
        }
        if (unknownGroupSizes.isEmpty() || targetGroup.isEmpty()) {
//             // println("allNotDefoBroken2 returning 1 for $unknownGroupSizes $targetGroup")
            return 1L
        }

        if (unknownGroupSizes.size == 1) {
            return allUnknown(unknownGroupSizes.first(), targetGroup).also {
//                println("allNotDefoBroken2 earlyout $it for groups=${unknownGroupSizes.map { "?".repeat(it) }} target=$targetGroup")

            }
        }

        var total = 0L
//        println("starting loop")
        for (iteration in (0..targetGroup.size)) {
            val left = allUnknown(unknownGroupSizes.first(), targetGroup.safeSubList(0, targetGroup.size - iteration))
            if (left > 0) {
//                println("Running  allNotDefoBroken2 with iteration=$iteration indicies=${targetGroup.indices} left=${targetGroup.safeSubList(0, targetGroup.size - x)} right=${targetGroup.safeSubList(targetGroup.size - x, targetGroup.size)}")
                val right = allNotDefoBroken2(
                    id,
                    unknownGroupSizes.safeSubList(1, unknownGroupSizes.size),
                    targetGroup.safeSubList(targetGroup.size - iteration, targetGroup.size)
                )
//                println("left=$left right=$right left=[group=${"?".repeat(unknownGroupSizes.first())}, target=${targetGroup.safeSubList(0, targetGroup.size - iteration)}], right=[groups=${unknownGroupSizes.safeSubList(1, unknownGroupSizes.size).map { "?".repeat(it) }}, target=${targetGroup.safeSubList(targetGroup.size - iteration, targetGroup.size)}")
                total += left * right
            }
        }
//        println("ending  loop")

//        println("allNotDefoBroken2 returning total=$total sizes=${unknownGroupSizes.map { "?".repeat(it) }} target=$targetGroup ")
        return total
    }

    fun <T> List<T>.removeSurrounding(element: T) =
        safeSubList(countWhile { it == element }, size - asReversed().countWhile { it == element })

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
//                   println("Found arrangement " + springs.joinToString(""))
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
//            // // // println("size $start $this")
            return false
        }

        for ((index, item) in start.withIndex()) {
            if (item != this[index]) {
//                // // // println("return false $start $this")
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
        groupSize: Int,
        targetGroup: List<Int>
    ): Long {
        if (groupSize <= 0 || groupSize < targetGroup.sum() || (groupSize == targetGroup.sum() && targetGroup.size > 1)) {
//            println("$groupSize $targetGroup returning 0")
            return 0L
        }

        if (targetGroup.isEmpty()) {
            return 1L
        }

        if (targetGroup == listOf(1)) {
//               println("$groupSize $targetGroup returning ${groupSize}")
            return groupSize.toLong()
        }


        val remainingUnknowns = groupSize - targetGroup.sum()
        val mustBeDots = targetGroup.size - 1
        val possibleSpaces = targetGroup.size + 1
        val freeDots = remainingUnknowns - mustBeDots

        if (freeDots < 0) {
            return 0L
        }

//          println("${"?".repeat(groupSize)} target=$targetGroup  $remainingUnknowns $mustBeDots $possibleSpaces $freeDots")

        return CombinatoricsUtils.binomialCoefficient(freeDots + possibleSpaces - 1, possibleSpaces - 1).also {
//             println("allUnknown Result $it for $groupSize $targetGroup")
        }
    }


    override fun runPart2(data: List<String>, runMode: RunMode): Long {
        val parsed = Parser.parsePart2(data)
        // // // println(parsed.size)
        var count = 0
        return parsed.take(1).sumOf { entry ->
//            countArrangementsOld(entry).also {
//                println("OLD $it | ${entry.springs.joinToString("")}")
//
//            }

            println(entry.springs.joinToString(""))
            countArrangements(entry).also {
                   println("Count ${count++}")

            }
        }
    }
}
