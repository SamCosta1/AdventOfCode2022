package y2023.day19

import puzzlerunners.Puzzle
import utils.*

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
//                is Not -> false
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

//        data class Not(val condition: Condition) : Condition() {
//            override val conditions: List<Condition>
//                get() = listOf(this)
//
//            override fun invert() = condition
//
//            override fun toString(): String {
//                return "NOT($condition)"
//            }
//        }

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
        val followResults = mutableListOf<List<Parser.WorkflowStep.Conditional>>()
        follow("in", workflows, emptyList(), followResults)

        val ranges = followResults.map { steps ->
            val ranges = mutableMapOf(
                's' to (1..4000L),
                'x' to (1..4000L),
                'a' to (1..4000L),
                'm' to (1..4000L),
            )

            steps.forEach { step ->
                val current = ranges[step.subject]!!
                ranges[step.subject] = when (step.operation) {
                    Parser.Operation.LessThan -> current.coerceAtMost(step.valueToCompare - 1)
                    Parser.Operation.LessThanOrEqual -> current.coerceAtMost(step.valueToCompare)
                    Parser.Operation.GreaterThan -> current.coerceAtLeast(step.valueToCompare + 1)
                    Parser.Operation.GreaterThanOrEqual -> current.coerceAtLeast(step.valueToCompare)
                }
            }
            ranges
        }

//        ranges.forEach { println(it) }
        val results = mapOf(
            's' to mutableSetOf<Long>(),
            'x' to mutableSetOf<Long>(),
            'a' to mutableSetOf<Long>(),
            'm' to mutableSetOf<Long>(),
        )

        var total: Long = 0

        ranges.forEach { entry: MutableMap<Char, LongRange> ->

            val distinctSizes = mutableMapOf<Char, Int>()
            entry.forEach { mapEntry ->
                val map = results[mapEntry.key]!!

                val distinctSize = mapEntry.value.toMutableSet().apply { removeAll(map) }.size

//                println("Distinct size $distinctSize for $mapEntry")
                distinctSizes[mapEntry.key] = distinctSize
                mapEntry.value.forEach { map.add(it) }
            }

            println("Distinct Sizes: $distinctSizes")
            if (distinctSizes.none { it.value == 0 }) {
                total += distinctSizes.values.productOfLong { it.toLong() }
            } else {
                distinctSizes.filter { it.value != 0 }.forEach { (label, value) ->
                    var partialResult = value.toLong()
                    entry.filter { it.key != label }.forEach {
                        partialResult *= it.value.count()
                    }
                    total += partialResult
                    println("Label $label $value partial: $partialResult")
                }
            }

            total++
        }


        total

    }
//    {s=1..1350, x=1..1415,    a=1..2005, m=1..4000}
//    {s=1..1350, x=1400..4000, a=1..2005, m=1..4000}

    val finishStates = setOf("A", "R")
    private fun follow(
        workflowName: WorkflowName,
        workflows: Map<WorkflowName, List<Parser.WorkflowStep>>,
        currentChain: List<Parser.WorkflowStep.Conditional>,
        results: MutableList<List<Parser.WorkflowStep.Conditional>>
    ) {
        if (workflowName == "R") {
//            println("Rejecting: $currentChain")
            return
        }

        if (workflowName == "A") {
            println("Accepting: $currentChain")
            results.add(currentChain)
            return
        }

        val workflow = workflows[workflowName]!!
        val inverts = workflow.mapNotNull { it.invert }

        workflow.forEachIndexed { index, step ->
            when (step) {
                is Parser.WorkflowStep.Conditional -> {
                    follow(
                        step.ifTrueResult,
                        workflows,
                        currentChain + inverts.take(index) + step,
                        results
                    )
                }

                is Parser.WorkflowStep.Default -> {
                    follow(
                        step.defaultResult,
                        workflows,
                        currentChain + inverts.take(index),
                        results
                    )
                }
            }

        }

    }

    private fun flatten(expression: Condition): List<Condition> {

        return when (expression) {
            is Condition.And -> {
                if (expression.conditions.any { it == Condition.False }) {
                    emptyList<Condition>()
                }

                val noTrues = expression.conditions.filterNot { it == Condition.True }
                val thingsToAnd = noTrues.mapNotNull { it as? Condition.Base } + noTrues.mapNotNull { condition ->
                    (condition as? Condition.And)?.conditions
                }.flatten()

                if (thingsToAnd.size == noTrues.size) {
                    return listOf(Condition.And(thingsToAnd))
                } else {
                    val ors = noTrues.mapNotNull { it as? Condition.Or }
                    ors.map { Condition.And(thingsToAnd + it.conditions) }.also {
                        println("ORS | $it")
                    }
                }
            }

            is Condition.Or -> expression.conditions.map { flatten(it) }.flatten().filterNot { it == Condition.False }
            is Condition.Base,
            Condition.True,
            Condition.False -> expression.conditions
//            is Condition.Not -> expression.conditions
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

