package y2024.day8

import utils.GenericGrid

object Parser {
    sealed class Item : GenericGrid.GenericGridItem {
        data object Outside: Item()
        data object Free: Item()
        data class Antenna(val frequency: Char): Item()

        override val char: String get() = when(this) {
            is Antenna -> "$frequency"
            Free -> "."
            Outside -> "@"
        }
    }
    fun parse(data: List<String>) = GenericGrid<Item>(Item.Outside).apply {
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                this[x, y] = when (char) {
                    '.' -> Item.Free
                    else -> Item.Antenna(char)
                }
            }
        }
    }
}