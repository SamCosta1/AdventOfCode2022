package y2023.day19

typealias WorkflowName = String

object Parser {
    data class Part(val x: Long, val m: Long, val a: Long, val s: Long)

    enum class Operation(val raw: String) {
        LessThan("<"),
        LessThanOrEqual("<="),
        GreaterThan(">"),
        GreaterThanOrEqual(">=")
    }

    sealed class WorkflowStep {
        val invert: WorkflowStep.Conditional?
            get() = when (this) {
                is Conditional -> when (operation) {
                    Operation.LessThan -> copy(operation = Operation.GreaterThanOrEqual)
                    Operation.LessThanOrEqual -> copy(operation = Operation.GreaterThan)
                    Operation.GreaterThan -> copy(operation = Operation.LessThanOrEqual)
                    Operation.GreaterThanOrEqual -> copy(operation = Operation.LessThan)
                }

                is Default -> null
            }

        data class Conditional(
            val subject: Char,
            val operation: Operation,
            val ifTrueResult: String,
            val valueToCompare: Long
        ) : WorkflowStep() {
            override fun toString(): String {
                return "{$subject${operation.raw}$valueToCompare}->$ifTrueResult"
            }
        }

        data class Default(val defaultResult: WorkflowName) : WorkflowStep() {
            override fun toString(): String {
                return "{$defaultResult}"
            }
        }
    }

    data class Info(val workflows: Map<WorkflowName, List<WorkflowStep>>, val parts: List<Part>)

    fun parse(data: List<String>): Info {
        val parts = data.drop(data.indexOfFirst { it.isBlank() } + 1).parseParts()
        val ops = data.take(data.indexOfFirst { it.isBlank() }).parseOps()

        return Info(workflows = ops, parts = parts)
    }

    private fun List<String>.parseParts() = map {
        it.removePrefix("{").removeSuffix("}").split(",")
    }.map { (x, m, a, s) ->
        Part(
            x = x.split("=").last().toLong(),
            m = m.split("=").last().toLong(),
            a = a.split("=").last().toLong(),
            s = s.split("=").last().toLong(),
        )
    }

    private fun List<String>.parseOps() = map {
        it.split("{")
    }.associate { (label, ops) ->
        val steps = ops.removeSuffix("}").split(",").map { rawStep ->
            if (rawStep.contains(":")) {
                val split = rawStep.split(":")
                WorkflowStep.Conditional(
                    subject = split[0][0],
                    operation = Operation.entries.first { it.raw == split[0][1].toString() },
                    valueToCompare = split[0].drop(2).toLong(),
                    ifTrueResult = split[1]
                )
            } else {
                WorkflowStep.Default(rawStep)
            }
        }
        label to steps
    }


}
