package y2023.day19

import puzzlerunners.Puzzle
import utils.*

typealias Part2State = Map<Char, LongRange>

class Main(
    override val part1ExpectedAnswerForSample: Any = 19114L,
    override val part2ExpectedAnswerForSample: Any = 167409079868000L,
    override val isComplete: Boolean = false,
) : Puzzle {

    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { info ->
        val ranges = mapOf(
            'x' to 1..4000L,
            'm' to 1..4000L,
            'a' to 1..4000L,
            's' to 1..4000L,
        )
        val validRanges = mutableListOf<Part2State>()
        computeValidRangesRecursive(validRanges, ranges, info.workflows, listOf(), "in")

        info.parts.filter { part ->
            validRanges.any {
                it['x']!!.contains(part.x)
                        && it['m']!!.contains(part.m)
                        && it['a']!!.contains(part.a)
                        && it['s']!!.contains(part.s)
            }
        }.sumOf { it.sum() }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).workflows.let { workflows ->
        val ranges = mapOf(
            'x' to 1..4000L,
            'm' to 1..4000L,
            'a' to 1..4000L,
            's' to 1..4000L,
        )

        val validRanges = mutableListOf<Part2State>()
        computeValidRangesRecursive(validRanges, ranges, workflows, listOf("in"), "in")

        validRanges.sumOf { range ->
            range.values.productOfLong { it.count().toLong() }
        }
    }

    private fun Parser.Info.computeValidRanges() = mutableListOf<Part2State>().apply {
        computeValidRangesRecursive(this, mapOf(
            'x' to 1..4000L,
            'm' to 1..4000L,
            'a' to 1..4000L,
            's' to 1..4000L,
        ), workflows, listOf("in"), "in")
    }
    private fun computeValidRangesRecursive(
        validRanges: MutableList<Part2State>,
        ranges: Part2State,
        workflows: Map<WorkflowName, List<Parser.WorkflowStep>>,
        chain: List<WorkflowName>,
        currentWorkflow: WorkflowName
    ) {

        // This combo is a dud
        if (currentWorkflow == "R") {
            return
        }

        if (currentWorkflow == "A") {
            validRanges.add(ranges)
            return
        }

        val steps = workflows[currentWorkflow]!!
        var currentRanges = ranges
        steps.forEach { step ->
            when (step) {
                is Parser.WorkflowStep.Conditional -> {
                    val newRanges = step.applyToRanges(currentRanges)
                    computeValidRangesRecursive(validRanges, newRanges, workflows, chain, step.ifTrueResult)
                    currentRanges = step.excludeFromRanges(currentRanges)
                }
                is Parser.WorkflowStep.Default -> {
                    computeValidRangesRecursive(validRanges, currentRanges, workflows, chain, step.defaultResult)
                }
            }
        }

    }
}