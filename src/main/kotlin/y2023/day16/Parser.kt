package y2023.day16

import utils.GenericGrid

object Parser {
    data class Tile(val type: Char): GenericGrid.GenericGridItem {
        override val char: String
            get() = type.toString()
        companion object {
            val offGrid = Tile(type = '%')
        }

    }

    fun parse(data: List<String>) = GenericGrid<Tile>(Tile.offGrid).also { grid ->
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                grid[x, y] = Tile(char)
            }
        }
    }
}