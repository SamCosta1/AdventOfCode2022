package y2021.day14

object Parser {
    data class Data(val template: String, val mappings: Map<String, String>)
    fun parse(data: List<String>) = Data(
        data.first(),
        data.drop(2).map {
            val split = it.split(" -> ")
            split[0] to split[1]
        }.toMap()
    )
}