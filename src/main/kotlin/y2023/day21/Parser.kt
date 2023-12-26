package y2023.day21

import utils.GenericGrid
import utils.Point

object Parser {
    data class Info(val start: Point, val grid: GenericGrid<Plot>)
    enum class Plot: GenericGrid.GenericGridItem {
        Garden,
        Rock,
        Mark;

        override val char: String
            get() = when (this) {
                Garden -> "."
                Rock ->  "#"
                Mark -> "O"
            }
    }
    fun parse(data: List<String>): Info {
        var start: Point? = null
        val grid = GenericGrid<Plot>(Plot.Rock)

        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                when (char) {
                    'S' -> {
                        start = Point(x, y)
                        grid[start!!] = Plot.Garden
                    }
                    '.' -> grid[x, y] = Plot.Garden
                    '#' -> grid[x, y] = Plot.Rock
                }
            }
        }

        return Info(start!!, grid)
    }
}