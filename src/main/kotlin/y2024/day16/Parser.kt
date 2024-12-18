package y2024.day16

import utils.GenericGrid

object Parser {
    sealed class Item(override val char: String): GenericGrid.GenericGridItem {
        data object Wall: Item("#")
        data object Start: Item("S")
        data object Free: Item(".")
        data object End: Item("E")

    }
    fun parse(data: List<String>) = GenericGrid<Item>(Item.Wall).apply {
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                this[x, y] = listOf(Item.Wall, Item.Start, Item.Free, Item.End).first { it.char == c.toString() }
            }
        }
    }
}