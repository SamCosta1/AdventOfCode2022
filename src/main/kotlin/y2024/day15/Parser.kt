package y2024.day15

import utils.GenericGrid
import utils.MovementDirection
import utils.RunMode
import utils.split

object Parser {
    sealed class Item(override val char: String): GenericGrid.GenericGridItem {
        fun isBox() = this == Box || this == BoxLeft || this == BoxRight

        data object Wall: Item("#")
        data object Box: Item("O")
        data object BoxLeft: Item("[")
        data object BoxRight: Item("]")
        data object Free: Item(" ")
        data object Robot: Item("@")
    }

    fun parse(data: List<String>, part: Int): Pair<GenericGrid<Item>, List<MovementDirection>> {
        val grid = GenericGrid<Item>(Item.Wall)
        val splitted = data.split { it.isBlank() }

        splitted[0].map {
            when(part) {
                1 -> it
                2 -> buildString {
                    it.forEach { char ->
                        when(char) {
                            'O' -> {
                                append('[')
                                append(']')
                            }
                            '@' -> {
                                append(char)
                                append('.')
                            }
                            else -> {
                                append(char)
                                append(char)
                            }
                        }
                    }
                }
                else -> throw Exception("idk what part this is")
            }
        }.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                grid[x, y] = when(char) {
                    '#' -> Item.Wall
                    '.' -> Item.Free
                    '[' -> Item.BoxLeft
                    ']' -> Item.BoxRight
                    'O' -> Item.Box
                    '@' -> Item.Robot
                    else -> throw Exception("uh oh")
                }
            }
        }

        val moves = splitted[1].map { row ->
            row.map { char ->
                MovementDirection.entries.first { it.char == char }
            }
        }.flatten()

        return grid to moves
    }
}