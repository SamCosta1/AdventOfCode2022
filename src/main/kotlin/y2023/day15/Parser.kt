package y2023.day15

object Parser {

    data class Instruction(val label: String, val op: Char, val focalLength: Int?)

    fun parse(data: List<String>) = data.first().split(",").map {
        val opIsEquals = it.contains("=")
        Instruction(
            if (opIsEquals) it.split("=").first() else it.removeSuffix("-"),
            if (opIsEquals) '=' else '-',
            if (opIsEquals) it.split("=").last().toInt() else null
        )
    }
}