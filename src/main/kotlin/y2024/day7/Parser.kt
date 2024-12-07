package y2024.day7

object Parser {
    data class Equation(val target: Long, val operands: List<Long>)
    fun parse(data: List<String>) = data.map { row ->
        Equation(
            row.split(":")[0].toLong(),
            row.split(":")[1].trim().split(" ").map { it.toLong() }
        )
    }
}