package y2022.day4

import java.nio.file.Files
import java.nio.file.Paths

object Day4Main {

    val data = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day4/data.txt"))

    fun run() = data.filter { raw ->
        val ranges = raw.split(",").map { range ->
            range.split("-").map { it.toInt() }
        }.sortedByDescending { it.last() }.sortedBy { it.first() }

        val rhsFirst = ranges.first().last()
        ranges.all { it.last() <= rhsFirst }
    }.count()

    fun runPart2() = data.size - data.filter { raw ->
        val ranges = raw.split(",").map { range ->
            range.split("-").map { it.toInt() }
        }.sortedByDescending { it.last() }.sortedBy { it.first() }

        val rhsFirst = ranges.first().last()
        ranges.drop(1).all { rhsFirst < it.first() } // don't overlap
    }.count()
}