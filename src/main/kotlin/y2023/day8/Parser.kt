package y2023.day8

object Parser  {
    enum class Instruction {
        Left,
        Right
    }
    data class Info(val instructions: List<Instruction>, val nodes: Map<String, Node>)
    data class Node(
        val label: String,
        var left: Node?,
        var right: Node?
    ) {
        override fun toString(): String {
            return "$label = (${left?.label}, ${right?.label})"
        }

        fun linkFor(instruction: Instruction): Node {
            return when(instruction) {
                Instruction.Left -> left!!
                Instruction.Right -> right!!
            }
        }
    }
    fun parse(data: List<String>): Info {
        val instructions = data.first().map {
            when (it) {
                'L' ->Instruction.Left
                'R' -> Instruction.Right
                else -> throw Exception("Unknown char")

            }
        }

        val rawParsedData = mutableMapOf<String, Pair<String, String>>()
        data.drop(2).forEach { row ->
            val split = row.split(" = ")
            val leftRight = split.last().removePrefix("(").removeSuffix(")").split(", ")
            rawParsedData[split.first()] = Pair(leftRight.first(), leftRight.last())
        }
        val tempNodes = rawParsedData.keys.associateWith { Node(label = it, null, null) }.toMutableMap()
        tempNodes.forEach { entry ->
            val leftRightPair = rawParsedData[entry.key]!!
            tempNodes[entry.key]?.left = tempNodes[leftRightPair.first]
            tempNodes[entry.key]?.right = tempNodes[leftRightPair.second]
        }

        return Info(instructions, tempNodes)
    }
}