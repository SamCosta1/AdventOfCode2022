package y2023.day19

import puzzlerunners.Puzzle
import utils.*

typealias Part2State = Map<Char, LongRange>

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

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).workflows.let { workflows ->
        val ranges = mapOf(
            'x' to 1..4000L,
            'm' to 1..4000L,
            'a' to 1..4000L,
            's' to 1..4000L,
        )
        println(listOf(1..4000).first().count())

        run(ranges, workflows, listOf("in"), "in")

        println(validRanges.take(3).joinToString("\n"))
        postProcess('x', target = validRanges.take(3))
    }

    val subjects = listOf('x', 'm', 'a', 's')
    fun postProcess(subject: Char, target: List<Part2State>): Long {
        if (subject == subjects.last()) {
            val numbers = mutableSetOf<Long>()
            target.map { it[subject]!! }.forEach { range ->
                range.forEach { numbers.add(it) }
            }

            println("s|${target.map { it[subject]!! }}")
            return numbers.size.toLong()
        }
        val subjectRanges =
            target.asSequence().map { it[subject]!! }.map { listOf(it.first, it.last) }.flatten().toSet().sorted()

        val rangesToIterate = mutableListOf<LongRange>()

        subjectRanges.forEachIndexed { index, l ->
            if (index != subjectRanges.lastIndex) {
                rangesToIterate.add(l..subjectRanges[index + 1])
            }
        }
        return rangesToIterate.sumOf { range ->
            val resultsToCheck = target.filter { it[subject]!!.contains(range) }
            val a = range.count() * postProcess(subjects[subjects.indexOf(subject) + 1], resultsToCheck)
            println("$subject| ${range} | $a")
            a
        }
    }

    var validRanges = mutableListOf<Part2State>()
    fun run(
        ranges: Part2State,
        workflows: Map<WorkflowName, List<Parser.WorkflowStep>>,
        chain: List<WorkflowName>,
        nextWorkflow: WorkflowName
    ) {
        // This combo is a dud
        if (nextWorkflow == "R") {
            return
        }

        if (nextWorkflow == "A") {
            validRanges.add(ranges)
            return
        }

        var currentRanges = ranges
        for (step in workflows[nextWorkflow]!!) {
            when (step) {
                is Parser.WorkflowStep.Conditional -> {
                    val subRanges = step.applyToRanges(currentRanges)
                    currentRanges = step.invert.applyToRanges(currentRanges)
                    run(subRanges, workflows, chain + nextWorkflow, step.ifTrueResult)
                }

                is Parser.WorkflowStep.Default -> run(ranges, workflows, chain + nextWorkflow, step.defaultResult)
            }
        }
    }
}

fun LongRange.contains(other: LongRange) = first <= other.first && last >= other.last