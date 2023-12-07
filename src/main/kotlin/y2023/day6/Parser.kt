package y2023.day6

object Parser {
    data class Race(val timeMs: Long, val distanceMm: Long)
    fun parse(data: List<String>): List<Race> {
        val times = data.first().removePrefix("Time:").trim().split(" ").map { it.toLong() }
        val distances = data.last().removePrefix("Distance:").trim().split(" ").map { it.toLong() }

        return times.mapIndexed() { index, time ->
           Race(time, distances[index])
        }
    }

    fun parsePart2(data: List<String>): Race {
        val time = data.first().removePrefix("Time:").filterNot { it.isWhitespace() }.toLong()
        val distance = data.last().removePrefix("Distance:").filterNot { it.isWhitespace() }.toLong()

        return Race(time, distance)

    }
}