package y2023.day10

import utils.GenericGrid
import utils.Point

fun GenericGrid<Parser.PipeItem>.connectedPoints(
    point: Point
): List<Point> = this[point].char.connectedPoints(point)
fun String.connectedPoints(point: Point) = when (this) {
    "│" -> listOf(point.top, point.bottom)
    "─" -> listOf(point.left, point.right)
    "└" -> listOf(point.top, point.right)
    "┘" -> listOf(point.left, point.top)
    "┐" -> listOf(point.left, point.bottom)
    "┌" -> listOf(point.bottom, point.right)
    else -> emptyList()
}

fun GenericGrid<Parser.PipeItem>.determinePipeItem(point: Point) = adjacentPoints(point).filter {
    (it.x == point.x || it.y == point.y) && this[it].char.isNotBlank() && this[it].char.connectedPoints(it).contains(point)
}.let { actualConnectedPoints ->

    val result = listOf("│",
        "─",
        "└",
        "┘",
        "┐",
        "┌").first { it.connectedPoints(point) == actualConnectedPoints }
    Parser.PipeItem(result)
}

fun followLoop(grid: GenericGrid<Parser.PipeItem>, startPoint: Point, onNewPipeItem: (Point) -> Unit) {
    var current = startPoint
    var previous: Point? = null

    do {
        current = grid.connectedPoints(current).first { it != previous }.also {
            previous = current
        }
        onNewPipeItem(current)
    } while (current != startPoint)
}