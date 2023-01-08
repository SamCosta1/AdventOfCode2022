package day3

import java.nio.file.Files
import java.nio.file.Paths

object Day3Main {
    val data = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/day3/data.txt"))

    fun run() = data.map { raw ->
        val section1 = raw.take(raw.length / 2).toSet()
        val section2 = raw.takeLast(raw.length / 2).toSet()

        val shortest_longest = listOf(section1, section2).sortedBy { it.size }
        val shortest = shortest_longest.first()
        val longest = shortest_longest.last()

        val commonChars = shortest.filter { longest.contains(it) }
        commonChars.sumBy { it.priority() }
    }.sum()

    fun runPart2() = data.chunked(3).map { list ->
        val commonChars = list.first().filter { list[1].contains(it) && list[2].contains(it) }.toSet()
        commonChars.sumBy { it.priority() }
    }.sum()

    fun Char.priority() = ("abcdefghijklmnopqrstuvwxyz" + "abcdefghijklmnopqrstuvwxyz".toUpperCase()).indexOf(this) + 1
}