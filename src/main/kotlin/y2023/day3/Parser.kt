package y2023.day3

import utils.GenericGrid
import y2022.day15.Point
import y2022.day7.Day7Main.debug
import kotlin.math.max
import kotlin.math.min


object Parser {
    private val numberRegex = "(\\d+)".toRegex()
    fun parse(data: List<String>) = GenericGrid<GridItem>(GridItem.Dot).also { grid ->
        data.mapIndexed { rowIndex, row ->
            // Numbers
            numberRegex.findAll(row).forEach { matchResult ->
                val numberId = "($rowIndex,${matchResult.range.first})"
                matchResult.range.forEach {
                    grid.set(
                        x = it,
                        y = rowIndex,
                        GridItem.Number(numberId, matchResult.groupValues.first().toLong())
                    )
                }
            }

            // Symbols
            row.forEachIndexed { index, char ->
                if (char.isSymbol) {
                    grid.set(x = index, y = rowIndex, GridItem.Symbol(char))
                }
            }
        }
    }

    private val Char.isSymbol get() = toString().let { charString ->
        charString != "." && charString.toIntOrNull() == null
    }

    sealed class GridItem: GenericGrid.GenericGridItem {
        override val char
            get() = when (this) {
                is Number -> "$value"
                is Symbol -> "$value"
                Dot -> "."
            }

        data class Number(val id: String, val value: Long) : GridItem()
        data class Symbol(val value: Char) : GridItem()
        object Dot : GridItem()
    }
}
