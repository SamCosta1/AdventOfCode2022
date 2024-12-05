package y2024.day5

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 143L,
    override val part2ExpectedAnswerForSample: Any = 123L,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (pairs, updates) ->
        val comparator = createComparator(pairs)
        updates.sumOf {
            val sorted = it.sortedWith(comparator)
            if (sorted == it) {
                sorted[it.size / 2].toLong()
            } else {
                0L
            }
        }
    }
    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (pairs, updates) ->
        val comparator = createComparator(pairs)
        updates.sumOf {
            val sorted = it.sortedWith(comparator)
            if (sorted != it) {
                sorted[it.size / 2].toLong()
            } else {
                0L
            }
        }
    }

    private fun createComparator(pairs: List<Pair<String, String>>): (String, String) -> Int {
        val lessThan = pairs.groupBy { it.second }.mapValues { entry -> entry.value.map { it.first } }
        val moreThan = pairs.groupBy { it.first }.mapValues { entry -> entry.value.map { it.second } }

        val comparator = { str1: String, str2: String ->
            if (str1 == str2) {
                0
            } else if (lessThan[str1]?.contains(str2) == true || moreThan[str2]?.contains(str1) == true) {
                1
            } else if (lessThan[str2]?.contains(str1) == true || moreThan[str1]?.contains(str2) == true) {
                -1
            } else {
                throw Exception("$str1 $str2")
            }
        }
        return comparator
    }
}
