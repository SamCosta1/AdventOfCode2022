package y2022.day21

import java.nio.file.Files
import java.nio.file.Paths

sealed class Action {
    companion object {
        val empty = Value(-1)
    }

    enum class Operation {
        Sum,
        Subtract,
        Multiply,
        Divide;

        override fun toString() = when (this) {
            Sum -> "+"
            Subtract -> "-"
            Multiply -> "*"
            Divide -> "/"
        }

        companion object {
            fun from(raw: String) = values().first { raw == it.toString() }
        }
    }

    data class Value(val value: Long) : Action()
    data class PerformOperation(
        val val1: Monkey,
        val val2: Monkey,
        val op: Operation
    ) : Action() {
        override fun toString(): String {
            return "${val1.name} $op ${val2.name}"
        }
    }
}

data class Monkey(val name: String, var action: Action)
object Parser {
    private val rhsRegex = "(.*) ([-+*/]) (.*)".toRegex()
    fun parse(file: String): List<Monkey> {
        val rawMap = Files.readAllLines(
            Paths.get(
                System.getProperty("user.dir"),
                "src/main/kotlin/y2022/day21/$file"
            )
        ).map { raw ->
            raw.split(":")
        }

        val monkies = rawMap.map { Monkey(it.first(), Action.empty) }
        monkies.forEachIndexed { index, monkey ->
            monkey.action = rawMap[index].last().trim().toLongOrNull()?.let {
                Action.Value(it)
            } ?: run {
                val groups = rhsRegex.matchEntire(rawMap[index].last().trim())!!
                    .groupValues
                    .drop(1)

                Action.PerformOperation(
                    monkies.first { it.name == groups.first() },
                    monkies.first { it.name == groups.last() },
                    Action.Operation.from(groups[1])
                )
            }
        }
        return monkies
    }
}