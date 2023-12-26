package y2023.day18

import utils.GenericGrid
import utils.MovementDirection

object Parser {
    data class InputLine(val direction: MovementDirection, val amount: Long, val hash: String): GenericGrid.GenericGridItem {
        override val char: String = when {
            amount <0 -> "."
//            amount>100 -> amount.toString().last().toString()
            else -> "#"
        }

        val isTrench = amount > 0
    }

    fun parse(data: List<String>) = data.map { row ->
        val split = row.split(" ")
        InputLine(
            day18MovementDirection(split[0][0]),
            split[1].toLong(),
            split.last().removePrefix("(").removeSuffix(")")
        )
    }

    fun day18MovementDirection(char: Char) = when (char) {
        'L' -> MovementDirection.West
        'R' -> MovementDirection.East
        'U' -> MovementDirection.North
        'D' -> MovementDirection.South
        else -> throw Exception()
    }
}