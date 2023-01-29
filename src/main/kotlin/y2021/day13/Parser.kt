package y2021.day13

import utils.Point

object Parser {
    data class Instruction(val axis: String, val line: Long)
    data class Data(val points: List<Point>, val instructions: List<Instruction>)

    fun pointsToString(points: Collection<Point>, instruction: Instruction?) = buildString {
        val maxX = points.maxOf { it.x }
        val maxY = points.maxOf { it.y }

        appendLine()
        (0..maxY).forEach { y ->
            (0..maxX).forEach { x ->
                if (instruction?.axis == "x" && x == instruction.line) {
                    append("|")
                } else if (instruction?.axis == "y" && y == instruction.line) {
                    append("-")
                } else if (points.contains(Point(x,y))) {
                    append("█")
                } else {
                    append(" ")
                }
            }
            appendLine()
        }
    }
    fun parse(data: List<String>): Data {
        val points = data.filter { it.contains(",") }.map {
            val raw = it.split(",")
            Point(raw.first().toLong(), raw.last().toLong())
        }

        val instructions = data.filter { it.startsWith("fold") }.map {
            val pair = it.split(" ").last().split("=")
            Instruction(pair.first(), pair.last().toLong())
        }
        return Data(points, instructions)
    }
}