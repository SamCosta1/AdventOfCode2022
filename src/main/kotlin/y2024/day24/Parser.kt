package y2024.day24

import utils.split

object Parser {
    data class Info(val initialValues: Map<String, Int>, val instructions: List<Instruction>) {
        data class Instruction(val op1: String, val op2: String, val output: String, val operation: Operation)
        enum class Operation {
            And,
            Or,
            Xor;

            override fun toString() = when(this) {
                And -> "AND"
                Or -> "OR"
                Xor -> "XOR"
            }
        }
    }

    fun parse(data: List<String>): Info {
        val initials = data.split { it.isBlank() }.first().map { row ->
            row.split(": ").let { it.first() to it.last().toInt() }
        }

        val instructions = data.split { it.isBlank() }.last().map { row ->
            val (left, output) = row.split(" -> ")
            val (op1, operation, op2) = left.split(" ")
            Info.Instruction(op1, op2, output, Info.Operation.entries.first { operation == it.toString() })
        }
        return Info(initials.toMap(), instructions)
    }
}