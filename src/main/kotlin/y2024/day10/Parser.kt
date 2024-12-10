package y2024.day10

import utils.GenericGrid

object Parser {
    data class Item(val value: Int): GenericGrid.GenericGridItem {
        override val char: String
            get() = value.toString()
    }

    fun parse(data: List<String>) =  GenericGrid<Item>(Item(-1)).apply {
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, value ->
                this[x, y] = Item(value.digitToInt())
            }
        }
    }
}