package y2024.day22

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 37327623L,
    override val part2ExpectedAnswerForSample: Any = 23L,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.map { it.toLong() }.sumOf {
        getSecretNumber(it)
    }

    private fun getSecretNumber(start: Long): Long {
        var current = start
        repeat(2000) {
            val x64 = current*64
            current = current.xor(x64) % 16777216

            val div32 = current / 32
            current = current.xor(div32) % 16777216

            val x2048 = current * 2048
            current = current.xor(x2048) % 16777216
        }
        return current
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = data.map { it.toLong() }.let { buyers ->
        val results = mutableMapOf<List<Long>, Long>()
        buyers.forEach {  initial ->
            var current = initial
            val currentSeq = mutableListOf<Long>()
            val resultsThisBuyer = mutableMapOf<List<Long>, Long>()
            repeat(2000) {
                val prev = current
                val x64 = current*64
                current = current.xor(x64) % 16777216

                val div32 = current / 32
                current = current.xor(div32) % 16777216

                val x2048 = current * 2048
                current = current.xor(x2048) % 16777216
                if (currentSeq.size == 4) {
                    currentSeq.removeFirstOrNull()
                }
                currentSeq.add((current % 10) - (prev % 10))

                if (currentSeq.size == 4 && !resultsThisBuyer.contains(currentSeq)) {
                    val im = currentSeq.toList()
                    resultsThisBuyer[im] = current % 10
                }
            }
            resultsThisBuyer.forEach { key, value ->
                results[key] = results.getOrDefault(key, 0) + value
            }
        }
        println(results[listOf(-2,1,-1,3)])
        results.values.max()
    }
}
