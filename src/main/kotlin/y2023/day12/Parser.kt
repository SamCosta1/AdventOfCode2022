package y2023.day12

object Parser {
    enum class SpringState {
        Broken,
        Unknown,
        Working;

        companion object {
            fun from(char: Char) = when(char) {
                '?' -> Unknown
                '#' -> Broken
                '.' -> Working
                else -> throw Exception("Unknown char")
            }
        }

        override fun toString(): String {
            return when(this) {
                Broken -> "#"
                Unknown -> "?"
                Working -> "."
            }
        }
    }

    data class LogEntry(val springs: List<SpringState>, val damagedGroups: List<Int>)
    fun parse(data: List<String>) = data.map { row ->
        val (springs, groups) = row.split(" ")
        LogEntry(
            springs = springs.map { SpringState.from(it) },
            damagedGroups = groups.split(",").map { it.toInt() }
        )
    }
    fun parsePart2(data: List<String>) = data.map { row ->
        val (springs, groups) = row.split(" ")

        LogEntry(
            springs = listOf(springs, springs, springs, springs, springs).joinToString("?").map { SpringState.from(it) },
            damagedGroups = listOf(groups,groups,groups,groups,groups).joinToString(",").split(",").map { it.toInt() }
        )
    }
}