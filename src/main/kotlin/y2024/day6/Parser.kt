package y2024.day6

import utils.GenericGrid
import utils.Point

object Parser {
    enum class Item: GenericGrid.GenericGridItem {
        Outside,
        Obstacle,
        Free;

        override val char: String get() = when(this) {
            Obstacle -> "#"
            Free -> "."
            Outside -> "@"
        }
    }
    fun parse(data: List<String>): Pair<Point, GenericGrid<Parser.Item>> = GenericGrid(Item.Outside).let { grid ->
        var guardPos: Point? = null
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c == '^') {
                    guardPos = Point(x, y)
                    grid[guardPos!!] = Item.Free
                } else {
                    grid[Point(x, y)] = Item.entries.first { it.char == c.toString() }
                }
            }
        }
        Pair(guardPos!!, grid)
    }
}