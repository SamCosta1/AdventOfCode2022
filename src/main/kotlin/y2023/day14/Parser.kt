package y2023.day14

import utils.GenericGrid
import utils.Point

object Parser {
    enum class Rock: GenericGrid.GenericGridItem {
        Round,
        None,
        Square;

        override val char: String get() = when(this) {
            Round -> "O"
            None -> "."
            Square -> "#"
        }
        companion object {
            fun from(char: Char) = entries.first { it.char == char.toString() }
        }
    }
    fun parse(data: List<String>): Pair<GenericGrid<Rock>, List<Point>> = GenericGrid<Rock>(Rock.Square).let { grid ->
        val roundRockStartPoints = mutableListOf<Point>()
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                grid[x, y] = Rock.from(char).also {
                    if (it == Rock.Round) {
                        roundRockStartPoints += Point(x, y)
                    }
                }
            }
        }
        Pair(grid, roundRockStartPoints)
    }
}