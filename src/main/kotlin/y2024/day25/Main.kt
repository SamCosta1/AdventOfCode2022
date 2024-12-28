package y2024.day25

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 3,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (locks, keys) ->
        var count = 0
        locks.forEach {  lock ->
            keys.forEach { key ->
                if (key.mapIndexed { i, keyPin ->keyPin + lock[i]}.all { it <= key.size }) {
                    count++
                }
            }
        }
        count
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
