package y2023.day20

import puzzlerunners.Puzzle
import utils.RunMode
import utils.productOfLong

class Main(
    override val part1ExpectedAnswerForSample: Any = 11687500L,
    override val part2ExpectedAnswerForSample: Any = "No-Op",
    override val isComplete: Boolean = false,
) : Puzzle {
    data class Pulse(val from: String, val high: Boolean, val to: String)

    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { modules ->
        val pulses = ArrayDeque<Pulse>(1000)

        val broadCasterOutputs = modules["broadcaster"]!!.outputs

        var lowCount = 0L
        var highCount = 0L

        repeat(1000) { buttonPressIndex ->
            val (high, low) = pressButton(broadCasterOutputs, pulses, modules)
            lowCount += low
            highCount += high
        }

        highCount * lowCount
    }

    private fun pressButton(
        broadCasterOutputs: Sequence<String>,
        pulses: ArrayDeque<Pulse>,
        modules: Map<String, Parser.Module>,
        onPulse: (Pulse) -> Unit = {}
    ): Pair<Long, Long> {
        var lowCount = 0L
        var highCount = 0L
        broadCasterOutputs.forEach { pulses.add(Pulse("broadcaster", false, it)) }
        lowCount += broadCasterOutputs.count() + 1

        while (pulses.isNotEmpty()) {
            val lastPulse = pulses.removeFirst()

            val newPulses = when (val toModule = modules[lastPulse.to]) {
                is Parser.Module.Broadcaster -> throw Exception("Unexpected Broadcaster")
                is Parser.Module.Conjunction -> {
                    toModule.inputValues[lastPulse.from] = lastPulse.high
                    if (toModule.inputValues.all { it.value }) {
                        lowCount += toModule.outputs.count()
                        toModule.outputs.map { Pulse(toModule.label, false, to = it) }
                    } else {
                        highCount += toModule.outputs.count()
                        toModule.outputs.map { Pulse(toModule.label, true, to = it) }
                    }
                }

                is Parser.Module.FlipFlop -> if (!lastPulse.high) {
                    toModule.value = !toModule.value
                    if (toModule.value) {
                        highCount += toModule.outputs.count()
                    } else {
                        lowCount += toModule.outputs.count()
                    }
                    toModule.outputs.map { Pulse(toModule.label, toModule.value, to = it) }
                } else {
                    emptySequence()
                }

                null -> emptySequence()
            }

            newPulses.forEach {
                onPulse(it)
            }
            pulses.addAll(newPulses)
        }
        return (highCount to lowCount)
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { modules ->
        if (runMode == RunMode.Sample) {
            return@let "No-Op"
        }
        val pulses = ArrayDeque<Pulse>(1000)
        val broadCasterOutputs = modules["broadcaster"]!!.outputs
        var buttonPressIndex = 1L

        val relevantNodes = listOf("gj", "bc", "bx", "qq")
        val firstIndexes = relevantNodes.associateWith { 0L }.toMutableMap()

        while (firstIndexes.any { it.value == 0L }) {
            pressButton(broadCasterOutputs, pulses, modules) {
                if (relevantNodes.contains(it.from) && !it.high && firstIndexes[it.from] == 0L) {
                    firstIndexes[it.from] = buttonPressIndex
                }
            }
            buttonPressIndex++
        }
        firstIndexes.values.productOfLong { it }
    }
}
