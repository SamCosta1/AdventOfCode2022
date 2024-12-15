package y2024.day11

object Paarser {
    fun parse(data: List<String>) = data.first().split(" ").map { it.toLong() }
}