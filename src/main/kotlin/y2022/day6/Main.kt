package y2022.day6

import puzzlerunners.Puzzle
import utils.RunMode
import java.nio.file.Files
import java.nio.file.Paths

class Main(
    override val part1ExpectedAnswerForSample: Any = 11,
    override val part2ExpectedAnswerForSample: Any = 26,
    override val isComplete: Boolean = false
): Puzzle {

    override fun runPart1(data: List<String>, runMode: RunMode): Any {
        val segment = LimitedSizeQueue<Char>(4)
        var currentIndex = 0
        data.first().trim().toCharArray().forEachIndexed { index, char ->
            segment.add(char)
            currentIndex = index

            if (segment.toSet().count() == 4) {
                return (currentIndex + 1).toString()
            }
        }
        throw Exception("bad")
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any {
        val segment = LimitedSizeQueue<Char>(14)
        var currentIndex = 0
        data.first().trim().toCharArray().forEachIndexed { index, char ->
            segment.add(char)
            currentIndex = index

            if (segment.toSet().count() == 14) {
                return (currentIndex + 1).toString()
            }
        }
        throw Exception("bad")
    }
}

class LimitedSizeQueue<K>(private val maxSize: Int) : ArrayList<K>() {
    override fun add(k: K): Boolean {
        val r = super.add(k)
        if (size > maxSize) {
            removeRange(0, size - maxSize)
        }
        return r
    }

    val youngest: K?
        get() = get(maxSize - 1)
    val oldest: K?
        get() = get(0)

}