package y2023.day19

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 19114L,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
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
        val valueToCompare = when(step.subject) {
            'x' -> x
            'a' -> a
            'm' -> m
            's' -> s
            else -> throw Exception("invalid subject ${step.subject}")
        }
        val result = when (step.operation) {
            '<' -> valueToCompare < step.valueToCompare
            '>' -> valueToCompare > step.valueToCompare
            else -> throw Exception("invalid op ${step.operation}")
        }

        return step.ifTrueResult.takeIf { result }
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""



}

