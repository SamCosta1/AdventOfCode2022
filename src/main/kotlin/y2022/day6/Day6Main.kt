package y2022.day6

import java.nio.file.Files
import java.nio.file.Paths


object Day6Main {
    val data = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day6/data.txt")).first()

    fun run(): String {
        val segment = LimitedSizeQueue<Char>(4)
        var currentIndex = 0
        data.trim().toCharArray().forEachIndexed { index, char ->
            segment.add(char)
            currentIndex = index

            if (segment.toSet().count() == 4) {
                return (currentIndex + 1).toString()
            }
        }
        throw Exception("bad")
    }

    fun runPart2(): String {
        val segment = LimitedSizeQueue<Char>(14)
        var currentIndex = 0
        data.trim().toCharArray().forEachIndexed { index, char ->
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