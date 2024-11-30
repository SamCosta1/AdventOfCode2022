package y2022.day22

import y2022.day15.Point
import java.nio.file.Files
import java.nio.file.Paths

enum class Direction {
    Right,
    Down,
    Left,
    Up;

    fun afterApplying(newDirection: RotateDirection)= when(newDirection) {
        RotateDirection.Clockwise -> values()[Math.floorMod(values().indexOf(this) + 1, values().size)]
        RotateDirection.AntiClockwise -> values()[Math.floorMod(values().indexOf(this) - 1, values().size)]
    }

    val char get() = when(this) {
        Right -> '>'
        Down -> 'V'
        Left -> '<'
        Up -> '^'
    }
}

enum class RotateDirection {
    Clockwise,
    AntiClockwise;

    companion object {
        fun from(char: Char) = when(char) {
            'R' -> Clockwise
            'L' -> AntiClockwise
            else -> throw Exception("Invalid Char")
        }
    }
}
sealed class Instruction {
    data class Move(val amount: Int): Instruction()
    data class Rotate(val newDirection: RotateDirection): Instruction()
}

data class Data(val startPoint: Point, val grid: Grid, val instructions: List<Instruction>)

object Parser {
    fun parse(file: String) = Files.readAllLines(
        Paths.get(
            System.getProperty("user.dir"),
            "src/main/kotlin/y2022/day22/$file"
        )
    ).let { lines ->
        Data(
            Point(lines[0].indexOfFirst { it == Grid.State.Open.char[0] } + 1L, 1),
            lines.dropLast(1).parseGrid(),
            lines.last().parseInstructions()
        )
    }

    private fun List<String>.parseGrid(): Grid = Grid(maxOf { it.length }.toLong(), size.toLong()).also { grid ->
        forEachIndexed { y, line ->
            line.forEachIndexed { x, char ->
                grid[Point(x + 1L, y + 1L)] = Grid.State.from(char)
            }
        }
    }


    private fun String.parseInstructions(): List<Instruction> {
        val instructions = mutableListOf<Instruction>()
        var index = 0
        while(index < length) {
            if (get(index).isDigit()) {
                var subIndex = index
                var number = ""
                while(subIndex < length && get(subIndex).isDigit()) {
                    number += get(subIndex++).toString()
                }
                instructions.add(Instruction.Move(number.toInt()))
                index = subIndex
            } else {
                instructions.add(Instruction.Rotate(RotateDirection.from(get(index))))
                index++
            }

        }

        return instructions
    }
}