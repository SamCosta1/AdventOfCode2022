package y2024.day15

import utils.GenericGrid
import utils.MovementDirection
import utils.split

object Parser {
    sealed class Item(override val char: String): GenericGrid.GenericGridItem {
        data object Wall: Item("#")
        data object Box: Item("O")
        data object Free: Item(" ")
        data object Robot: Item("@")
    }

    fun parse(data: List<String>): Pair<GenericGrid<Item>, List<MovementDirection>> {
        val grid = GenericGrid<Item>(Item.Wall)
        val splitted = data.split { it.isBlank() }

        splitted[0].forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                grid[x, y] = when(char) {
                    '#' -> Item.Wall
                    '.' -> Item.Free
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