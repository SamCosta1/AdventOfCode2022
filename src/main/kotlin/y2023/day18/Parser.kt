package y2023.day18

import utils.GenericGrid
import utils.MovementDirection
import y2023.day3.Parser

object Parser {
    data class InputLine(val direction: MovementDirection, val amount: Long, val hash: String): GenericGrid.GenericGridItem {
        override val char: String = when {
            amount <0 -> "."
            amount>100 -> "O"
            else -> "#"
        }

        val isTrench = amount > 0
    }

    fun parse(data: List<String>) = data.map { row ->
        val split = row.split(" ")
        InputLine(
            when (split[0][0]) {
                'L' -> MovementDirection.West
                'R' -> MovementDirection.East
                'U' -> MovementDirection.North
                'D' -> MovementDirection.South
                else -> throw Exception()
            },
            split[1].toLong(),
            split.last().removeSuffix(")").removePrefix(" ")
        )
    }
}