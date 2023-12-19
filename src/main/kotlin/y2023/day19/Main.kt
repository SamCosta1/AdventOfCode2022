package y2023.day19

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 19114L,
    override val part2ExpectedAnswerForSample: Any = 167409079868000L,
    override val isComplete: Boolean = false,
) : Puzzle {
    enum class Result {
        Accept,
        Reject
    }

    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        determineEndState(info).filter { it.value == Result.Accept }.map {
            it.key.sum()
        }.sum()
    }

    private fun determineEndState(info: Parser.Info) = info.parts.associateWith { it.determineEndState(info.workflows) }


    private fun Parser.Part.determineEndState(
        workflows: Map<WorkflowName, List<Parser.WorkflowStep>>
    ): Result {
        var state = "in"
        val endStates = setOf("R", "A")


        while (state !in endStates) {
            val workflow = workflows[state]!!
            for (step in workflow) {
                when (step) {
                    is Parser.WorkflowStep.Conditional -> {
                        val result = this.determineResult(step)
                        if (result != null) {
                            state = result
                            break
                        }
                    }

                    is Parser.WorkflowStep.Default -> {
                        state = step.defaultResult
                        break
                    }
                }
            }
        }

        return when (state) {
            "R" -> Result.Reject
            "A" -> Result.Accept
            else -> throw Exception("eh $state")
        }
    }

    private fun Parser.Part.sum() = x + m + a + s
    private fun Parser.Part.determineResult(step: Parser.WorkflowStep.Conditional): String? {
        val valueToCompare = when (step.subject) {
            'x' -> x
            'a' -> a
            'm' -> m
            's' -> s
            else -> throw Exception("invalid subject ${step.subject}")
        }
        val result = when (step.operation) {
            Parser.Operation.LessThan -> valueToCompare < step.valueToCompare
            Parser.Operation.GreaterThan -> valueToCompare > step.valueToCompare
            else -> throw Exception("invalid op ${step.operation}")
        }

        return step.ifTrueResult.takeIf { result }
    }


    sealed class Condition {
        abstract val conditions: List<Condition>
        abstract fun invert(): Condition
        val isFlat
            get() = when (this) {
                is And -> conditions.none { it is And || it is Or }
                is Or -> conditions.none { it is And || it is Or }
                is Base -> true
                False -> true
                True -> true
                is Not -> false
            }

        data class Base(val step: Parser.WorkflowStep.Conditional) : Condition() {
            override val conditions: List<Condition>
                get() = listOf(this)

            override fun toString(): String {
                return "${step.subject}${step.operation.raw}${step.valueToCompare}"
            }

            override fun invert() = Base(step.invert!!)
        }

        data class Or(override val conditions: List<Condition>) : Condition() {
            override fun toString(): String {
                return "(${conditions.joinToString(" | ")})"
            }

            override fun invert() = And(conditions.map { it.invert() })
        }

        data class And(override val conditions: List<Condition>) : Condition() {
            override fun toString(): String {
                return "(${conditions.joinToString(" & ")})"
            }

            override fun invert() = Or(conditions.map { it.invert() })
        }

        data class Not(val condition: Condition) : Condition() {
            override val conditions: List<Condition>
                get() = listOf(this)

            override fun invert() = condition

            override fun toString(): String {
                return "NOT($condition)"
            }
        }

        data object True : Condition() {
            override val conditions: List<Condition>
                get() = listOf(this)

            override fun toString(): String {
                return "T"
            }

            override fun invert() = False

        }

        data object False : Condition() {
            override val conditions: List<Condition>
                get() = listOf(this)

            override fun toString(): String {
                return "F"
            }

            override fun invert(): Condition = True
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).workflows.let { workflows ->
        val fullExpression = process("in", workflows)

        println(fullExpression)
        val flatten = flatten(fullExpression).filterNot { it == Condition.False }
        println(flatten)
        val test = Condition.And(
            listOf(
                Condition.Base(
                    Parser.WorkflowStep.Conditional(
                        'x',
                        Parser.Operation.LessThan,
                        "a",
                        100
                    )
                ),
                Condition.And(
                    listOf(
                        Condition.And(
                            listOf(
                                Condition.Base(
                                    Parser.WorkflowStep.Conditional(
                                        'y',
                                        Parser.Operation.LessThan,
                                        "a",
                                        200
                                    )
                                ),
                                Condition.False
                            )
                        ),
                        Condition.False
                    )
                )
            )
        )
        println(test)
        println(flatten(test))

    }


    private fun flatten(expression: Condition): List<Condition> {

        return when (expression) {
            is Condition.And -> {
                if (expression.conditions.any { it == Condition.False }) {
                    emptyList()
                } else {
                    val toOr = expression.conditions.map {
                        flatten(expression.invert())
                    }.flatten()
                    if (toOr.any { it == Condition.True }) {
                        emptyList()
                    } else {
                        listOf(Condition.Not(Condition.Or(toOr)))
                    }
                }
            }

            is Condition.Or -> expression.conditions.map { flatten(it) }.flatten().filterNot { it == Condition.False }
            is Condition.Base,
            Condition.True,
            Condition.False -> expression.conditions
            is Condition.Not -> expression.conditions
        }


    }

    private fun process(
        workflowName: WorkflowName,
        workflows: Map<WorkflowName, List<Parser.WorkflowStep>>
    ): Condition {
        if (workflowName == "A") {
            return Condition.True
        } else if (workflowName == "R") {
            return Condition.False
        }
        val workflow = workflows[workflowName]!!

        val inverts = workflow.mapNotNull { it.invert }.map { Condition.Base(it) }
        val ors = workflow.mapIndexed { index, step ->
            Condition.And(
                inverts.take(index) + when (step) {
                    is Parser.WorkflowStep.Conditional -> listOf(
                        Condition.Base(step),
                        process(step.ifTrueResult, workflows)
                    )

                    is Parser.WorkflowStep.Default -> listOf(process(step.defaultResult, workflows))
                }
            )
        }
        return Condition.Or(ors)
    }


}

