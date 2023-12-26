package y2023.day22

import utils.Generic3dGrid
import utils.GenericGrid
import utils.Point3D

object Parser {

    sealed class GridItem : GenericGrid.GenericGridItem {
        data class Block(val id: String, val label: Char, var points: List<Point3D>) : GridItem() {
            val isVertical = !points.all { it.z == points.first().z }
        }

        data object Air : GridItem()

        override val char: String
            get() = when (this) {
                Air -> "."
                is Block -> this.label.toString()
            }

    }

    fun parse(data: List<String>) = data.mapIndexed { index, row ->
        val (startList, endList) = row.split("~").map { it.split(",").map { it.toInt() } }
        val (xStart, yStart, zStart) = startList
        val (xEnd, yEnd, zEnd) = endList

        val points = mutableListOf<Point3D>()
        (xStart..xEnd).forEach { x ->
            (yStart..yEnd).forEach { y ->
                (zStart..zEnd).forEach { z ->
                    points.add(Point3D(x, y, z))
                }
            }
        }

        GridItem.Block(
            "id$index",
            "ABCDEFGHIJKLMNOP"[index % 16],
            points
        )
    }.let { blocks ->
        Pair(blocks, Generic3dGrid<GridItem>(GridItem.Air).apply {
            blocks.forEach { block ->
                block.points.forEach { p ->
                    this[p] = block
                }
            }
        })
    }

}