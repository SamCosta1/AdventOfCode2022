package y2023.day10

import utils.GenericGrid
import utils.Point
import y2023.day3.Parser
import java.nio.channels.Pipe

object Parser {

    data class PipeItem(override val char: String) : GenericGrid.GenericGridItem {
        companion object {
            val empty = PipeItem('E')
        }

        constructor(rawChar: Char) : this(
            when (rawChar) {
                '|' -> "│"
                '-' -> "─"
                'L' -> "└"
                'J' -> "┘"
                '7' -> "┐"
                'F' -> "┌"
                '.' -> " "
                'S' -> "S"
                'E' -> " "
                else -> throw Exception("Unexpected char $rawChar")
            }
        )

        val isTile = char.isBlank()
    }

    data class Info(
        val grid: GenericGrid<PipeItem>,
        val startPoint: Point
    )

    fun parse(data: List<String>) = GenericGrid(PipeItem.empty).let { grid ->
        var startPoint: Point? = null
        data.forEachIndexed { yIndex, row ->
            row.forEachIndexed { xIndex, char ->
                if (char == 'S') {
                    startPoint = Point(xIndex, yIndex)
                }
                grid[Point(xIndex, yIndex)] = PipeItem((char))
            }
        }

        println(grid)
        grid[startPoint!!] = grid.determinePipeItem(startPoint!!)

        Info(grid, startPoint!!)
    }
}