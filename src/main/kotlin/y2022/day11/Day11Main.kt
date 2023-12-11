package y2022.day11

import puzzlerunners.Puzzle
import utils.RunMode
import java.math.RoundingMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 10605L,
    override val part2ExpectedAnswerForSample: Any = 2713310158L,
    override val isComplete: Boolean = false
) : Puzzle {
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

    private fun parse(data: List<String>) = data
        .chunked(7)
        .map { raw ->
            Monkey(
                name = raw.first().removePrefix("Monkey ").removeSuffix(":"),
                items = raw[1].removePrefix("  Starting items: ").split(", ").map { it.toLong() }.toMutableList(),
                operation = extractOperation(raw[2]),
                testDivisor = raw[3].removePrefix("  Test: divisible by ").toLong(),
                trueMonkeyName = raw[4].removePrefix("    If true: throw to monkey "),
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


    private fun executeRound(monkeys: List<Monkey>, superDivisor: Long, worryDivisor: Double) {

        monkeys.forEach { monkey ->
            monkey.items.forEach { item ->
                // Inspect
                val inspected = monkey.operation(item)
                monkey.numInspections++
                val postRelief = if (worryDivisor == 1.0) inspected else (inspected.toBigDecimal()
                    .divide(worryDivisor.toBigDecimal(), RoundingMode.FLOOR).toLong())

                val monkeyToThrow = if (postRelief % monkey.testDivisor == 0L) {
                    monkey.trueMonkeyName
                } else {
                    monkey.falseMonkeyName
                }
                monkeys.first { it.name == monkeyToThrow }.items.add(postRelief % superDivisor)
            }
            monkey.items.clear()

        }
    }

    override fun runPart1(data: List<String>, runMode: RunMode): Any {
        val monkeys = parse(data)
        val superDivisor = monkeys.map { it.testDivisor }.reduce { acc, l -> acc * l }

        return execute(monkeys = monkeys, rounds = 20, worryDivisor = 3.0, superDivisor)
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any {
        val parsed = parse(data)
        val superDivisor = parsed.map { it.testDivisor }.reduce { acc, l -> acc * l }

        return execute(monkeys = parsed, rounds = 10000, worryDivisor = 1.0, superDivisor)
    }

    private fun execute(monkeys: List<Monkey>, rounds: Int, worryDivisor: Double, superDivisor: Long): Long {
        repeat(rounds) {
            executeRound(monkeys, worryDivisor = worryDivisor, superDivisor = superDivisor)
        }

        return monkeys
            .sortedBy { it.numInspections }
            .takeLast(2)
            .map { it.numInspections }
            .reduce { acc, i -> i * acc }
    }
}