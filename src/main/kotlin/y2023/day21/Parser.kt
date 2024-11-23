package y2023.day21

import utils.GenericGrid
import utils.Point

object Parser {
    data class Info(val start: Point, val grid: GenericGrid<Plot>)
    enum class Plot : GenericGrid.GenericGridItem {
        Garden,
        Rock,
        Mark;

        override val char: String
            get() = when (this) {
                Garden -> "."
                Rock -> "#"
                Mark -> "O"
            }
    }

    class CustomGrid : GenericGrid<Plot>(Plot.Rock) {
        override fun get(point: Point): Plot {
            return super.get(
                Point(
                    x = point.x.mod(bottomRightMostPoint.x + 1),
                    y = point.y.mod(bottomRightMostPoint.y + 1)
                )
            )
        }
    }

    fun parse(data: List<String>): Info {
        var start: Point? = null
        val grid = CustomGrid()

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