package y2022.day11

import java.math.RoundingMode
import java.nio.file.Files
import java.nio.file.Paths

class Day11Main {
    data class Monkey(
        val name: String,
        var numInspections: Long = 0L,
        val items: MutableList<Long>,
        val operation: (Long) -> Long,
        val testDivisor: Long,
        val trueMonkeyName: String,
        val falseMonkeyName: String
    ) {
        fun simpleString() = "Monkey $name: $numInspections $items"
    }

    val data = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day11/data.txt"))
        .chunked(7)
        .map { raw ->
            Monkey(
                name = raw.first().removePrefix("Monkey ").removeSuffix(":"),
                items = raw[1].removePrefix("  Starting items: ").split(", ").map { it.toLong() }.toMutableList(),
                operation = extractOperation(raw[2]),
                testDivisor = raw[3].removePrefix("  Test: divisible by ").toLong(),
                trueMonkeyName =  raw[4].removePrefix("    If true: throw to monkey "),
                falseMonkeyName = raw[5].removePrefix("    If false: throw to monkey ")
            )
        }

    private fun extractOperation(raw: String): (Long) -> Long {
        val rhs = raw.removePrefix("  Operation: new = ").split(" ")

        return { old ->
            val op1 = if (rhs.first() == "old") old else rhs.first().toLong()
            val op2 = if (rhs.last() == "old") old else rhs.last().toLong()

            when (rhs[1]) {
                "+" -> op1 + op2
                "*" -> op1 * op2
                else -> throw Exception("Not supported $rhs")
            }
        }
    }

    val superDivisor by lazy { data.map { it.testDivisor }.reduce { acc, l -> acc * l } }

    fun executeRound(worryDivisor: Double) {
        data.forEach { monkey ->
            monkey.items.forEach { item ->
                // Inspect
                val inspected = monkey.operation(item)
                monkey.numInspections++
                val postRelief = if (worryDivisor == 1.0) inspected else (inspected.toBigDecimal().divide(worryDivisor.toBigDecimal(), RoundingMode.FLOOR).toLong())

                val monkeyToThrow = if (postRelief % monkey.testDivisor == 0L) {
                    monkey.trueMonkeyName
                } else {
                    monkey.falseMonkeyName
                }
                data.first { it.name == monkeyToThrow }.items.add(postRelief % superDivisor)
            }
            monkey.items.clear()

        }
    }
    fun run(): String {
        repeat(20) {
            executeRound(worryDivisor = 3.0)
        }

        return data
            .sortedBy { it.numInspections }
            .takeLast(2)
            .map { it.numInspections }
            .reduce { acc, i -> i * acc }.toString()
    }

    fun runPart2(): String {
        repeat(10000) {
            executeRound(worryDivisor = 1.0)
        }

        return data
            .sortedBy { it.numInspections }
            .takeLast(2)
            .map { it.numInspections }
            .reduce { acc, i -> i * acc }.toString()
    }
}