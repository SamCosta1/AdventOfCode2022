package y2023.day20

object Parser {
    sealed class Module {
        abstract val label: String
        abstract val outputs: Sequence<String>
        abstract val char: Char
        data class FlipFlop(
            override val label: String,
            var value: Boolean = false,
            override val outputs: Sequence<String>
        ): Module() {
            override val char: Char
                get() = '%'
        }
        data class Conjunction(
            override val label: String,
            var inputValues: MutableMap<String, Boolean>,
            override val outputs: Sequence<String>
        ): Module() {
            override val char: Char
                get() = '&'
        }

        data class Broadcaster(override val label: String = "broadcaster", override val outputs: Sequence<String>): Module() {
            override val char: Char
                get() = ' '
        }

    }
    fun parse(data: List<String>): Map<String, Module> {
        val modules = data.map { row ->
            val split = row.split("->")
            val outputs = split.last().trim().split(", ").asSequence()
            val start = split.first().trim()
            if (row.startsWith("broadcaster")) {
                Module.Broadcaster(outputs = outputs)
            } else if (row.startsWith("%")) {
                Module.FlipFlop(start.removePrefix("%"), outputs = outputs)
            } else if (!start.startsWith("&")) {
                throw Exception()
            } else {
                Module.Conjunction(start.removePrefix("&"), mutableMapOf(), outputs)
            }
        }.associateBy { it.label }

        val conjunctions = modules.values.mapNotNull { it as? Module.Conjunction }
        modules.values.forEach { module ->
            val conjunctionOutputs = conjunctions.filter { module.outputs.contains(it.label) }
            conjunctionOutputs.forEach {
                it.inputValues[module.label] = false
            }
        }

        return modules
    }
}