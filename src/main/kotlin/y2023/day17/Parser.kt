package y2023.day17

import utils.GenericGrid
import y2023.day3.Parser

object Parser {
    data class Block(val value: Int, val override: String? = null): GenericGrid.GenericGridItem {
        override val char: String
            get() = override ?: value.toString()

        companion object {
            val offGrid = Block(-1)
        }
    }
    fun parse(data: List<String>) = GenericGrid<Block>(Block.offGrid).also { grid ->
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                grid[x,y] = Block(c.digitToInt())
            }
        }
    }
}