package y2023.day11

import utils.GenericGrid
import utils.Point

object Parser {

    data class SpaceItem(val number: Int): GenericGrid.GenericGridItem {
        override val char: String = when(number) {
            -1 -> "."
            else -> "#"
        }

    }
    fun parse(data: List<String>, expansionAmount: Long): GenericGrid<SpaceItem> {
        val points = mutableListOf<Point>()
        var yOffsetsDueToExpansion = 0L
        for ((y, row) in data.withIndex()) {
            if (row.all { it == '.' }) {
                yOffsetsDueToExpansion += expansionAmount - 1
                continue
            }
            row.forEachIndexed { x, char ->
                if (char != '.') {
                    points += Point(x.toLong(), y + yOffsetsDueToExpansion)
                }
            }
        }
        val columnsToDuplicate = mutableListOf<Long>()
        (0..points.maxOf { it.x }).forEach { column ->
            if (points.all { it.x != column}) {
                columnsToDuplicate.add(column)
            }
        }

        val grid = GenericGrid(SpaceItem(-1))

        points.forEachIndexed { index, point ->
            grid[Point(point.x + columnsToDuplicate.count { point.x > it } * (expansionAmount - 1), point.y)] = SpaceItem(index + 1)
        }
        return grid
    }
}