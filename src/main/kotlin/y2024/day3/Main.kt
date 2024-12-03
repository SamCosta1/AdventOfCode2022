package y2024.day3

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 161L,
    override val part2ExpectedAnswerForSample: Any = 48L,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = data.sumOf { input -> multiplyAndSumInstructions(input) }

    private fun multiplyAndSumInstructions(input: String): Long {
        val regex = "(\\d{1,3}),(\\d{1,3})\\).*".toRegex()
        return input.split("mul(").mapNotNull { chunk ->
            val match = regex.matchEntire(chunk)
            val v1 = match?.groupValues?.getOrNull(1)?.toLongOrNull()
            val v2 = match?.groupValues?.getOrNull(2)?.toLongOrNull()
            if (v1 != null && v2 != null) {
                v1 * v2
            } else {
                null
            }
        }.sum()
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = data.joinToString("")
        .split("do()")
        .mapNotNull { it.split("don't()").firstOrNull() }
        .sumOf(::multiplyAndSumInstructions)
}
