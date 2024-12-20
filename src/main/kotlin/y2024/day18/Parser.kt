package y2024.day18

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

    fun parsePart1(data: List<String>, runMode: RunMode) = parse(data, runMode).let { (bytes, grid) ->
        bytes.take(
            when (runMode) {
                RunMode.Sample -> 12
                RunMode.Real -> 1024
            }
        ).forEach { grid[it] = Item.Corrupted }
        grid
    }

    fun parse(data: List<String>, runMode: RunMode) = GenericGrid<Item>(Item.Corrupted).let { grid ->
        val gridSize = when (runMode) {
            RunMode.Sample -> 6
            RunMode.Real -> 70
        }

        (0..gridSize).forEach { x ->
            (0..gridSize).forEach { y ->
                grid[x, y] = Item.Free
            }
        }
        val bytes = data.map { row ->
            val (x, y) = row.split(",").map { it.toLong() }
            Point(x, y)
        }
        bytes to grid
    }
}