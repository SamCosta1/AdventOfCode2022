package y2022.day17

import y2022.day15.Point
import java.nio.file.Files
import java.nio.file.Paths

enum class Direction {
    Left,
    Right;

    companion object {
        fun fromChar(char: Char) = when(char) {
            '<' -> Left
            '>' -> Right
            else -> throw Exception("Unknown character $char")
        }
    }
}

fun Point.apply(direction: Direction) = Point(x + when(direction) {
    Direction.Left -> -1
    Direction.Right -> +1
}, y)

object Parser {

    fun parse(file: String) = Files.readAllLines(
        Paths.get(
            System.getProperty("user.dir"),
            "src/main/kotlin/y2022/day17/$file"
        )
    ).joinToString("").map { Direction.fromChar(it) }
}