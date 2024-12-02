package y2024.day2

typealias Report = List<Int>
object Parser {
    fun parse(data: List<String>): List<Report> = data.map { report -> report.split(" ").map { it.toInt() } }
}