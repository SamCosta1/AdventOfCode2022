package y2024.day21

import kotlinx.coroutines.*
import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode


class Main(
    override val part1ExpectedAnswerForSample: Any = 126384L,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.sumOf {
        it.removeSuffix("A").toLong() * shortestSequence(it)
    }.also {
        runBlocking {
            println(Runtime.getRuntime().availableProcessors())
            val jobs = List(20) { index ->
                GlobalScope.launch(Dispatchers.Default) {
                    println("Task $index is running on thread: ${Thread.currentThread().name}")
                    delay(1000) // Simulate work
                }
            }
            jobs.joinAll()
        }
    }

    private fun shortestSequence(input: String): Long {
        return 0
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
