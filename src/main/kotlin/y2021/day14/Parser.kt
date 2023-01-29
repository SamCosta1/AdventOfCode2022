package y2021.day14

object Parser {
    data class Mapping(val source: String, val dest: String)
    data class Data(val template: String, val mappings: List<Mapping>)
    fun parse(data: List<String>) = Data(
        data.first(),
        data.drop(2).map {
            val split = it.split(" -> ")
            Mapping(split[0], split[1])
        }
    )
}