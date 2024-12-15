package y2024.day12

object Parser {
    fun parse(data: List<String>): Array<CharArray> {
        val padRow = listOf(buildString { repeat(data.first().length + 2) { append('@') } })
        return (padRow + data.map { "@$it@" } + padRow).map { it.toCharArray() }.toTypedArray()
    }
}