package y2024.day5

typealias Update = List<String>
object Parser {

    fun parse(data: List<String>): Pair<List<Pair<String, String>>, List<Update>> {
        val pairs = data.takeWhile { it.isNotBlank() }.map { raw ->
            raw.split("|").let {
                Pair(it[0], it[1])
            }
        }

        val updateRows = data.takeLastWhile { it.isNotBlank() }.map { it.split(",") }
        return Pair(pairs, updateRows)
    }
}