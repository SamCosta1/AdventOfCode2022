package y2024.day20

import utils.GenericGrid
import utils.Point
import utils.RunMode

object Parser {

    enum class Item : GenericGrid.GenericGridItem {
        Corrupted,
        Free;

        override val char: String
            get() = when (this) {
                Corrupted -> "#"
                Free -> "."
            }
    }

    fun parse(data: List<String>, runMode: RunMode) = GenericGrid<Item>(Item.Corrupted).let { grid ->
        val gridSize = data.size
        var start: Point? = null
        var end: Point? = null

        (0..<gridSize).forEach { x ->
            (0..<gridSize).forEach { y ->
                if (data[y][x] == 'S') {
                    start = Point(x, y)
                    grid[start!!] = Item.Free
                } else if (data[y][x] == 'E') {
                    end = Point(x, y)
                    grid[end!!] = Item.Free
                } else {
                    grid[x, y] = Item.entries.first { it.char == data[y][x].toString() }
                }
            }
        }
        Triple(grid, start!!, end!!)
    }
}