package y2023.day17

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.Point
import utils.RunMode
import kotlin.math.min

class Main(
    override val part1ExpectedAnswerForSample: Any = 102,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val points = mutableListOf<Point>(Point(0, 0))
        var x = 0
        var y = 0

        while (x <= grid.bottomRightMostPoint.x && y <= grid.bottomRightMostPoint.y) {
            points.add(Point(++x, y))
            points.add(Point(x, ++y))
        }
        val easyMin = points.sumOf { grid[it].value }
        val result = ResultContainer(easyMin)
        go(mutableListOf(Point(0, 0)), grid = grid, sumOfSoFar = 0, minSoFar = result)
        result.value
    }

    data class ResultContainer(var value: Int)

    fun go(soFar: List<Point>, grid: GenericGrid<Parser.Block>, sumOfSoFar: Int, minSoFar: ResultContainer) {

        println(soFar)
        if (soFar.contains(grid.bottomRightMostPoint)) {
            if (sumOfSoFar < minSoFar.value) {
                minSoFar.value = sumOfSoFar
            }
            return
        }

        val last = soFar.last()
        val canGoHorizontal = soFar.size < 3 || !soFar.takeLast(3).all { it.y == last.y }
        val canGoVertical = soFar.size < 3 || !soFar.takeLast(3).all { it.x == last.x }

        listOfNotNull(
            last.bottom.takeIf { canGoVertical },
            last.right.takeIf { canGoHorizontal },
            last.top.takeIf { canGoVertical && last.x < grid.bottomRightMostPoint.x },
            last.left.takeIf { canGoHorizontal && last.y < grid.bottomRightMostPoint.y },
        ).filter {
            !soFar.contains(it) && grid[it] != Parser.Block.offGrid
        }.forEach { neighbour ->
            val sumWithThis = sumOfSoFar + grid[neighbour].value
            if (sumWithThis < minSoFar.value) {
//                println("$sumOfSoFar $minSoFar | $soFar")

                go(soFar + neighbour, grid, sumWithThis, minSoFar)
            }
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}


//        val dist = grid.points.keys.associateWith { Int.MAX_VALUE - 2000 }.toMutableMap()
//        val previous = grid.points.keys.associateWith { null as? Point }.toMutableMap()
//
//        val q = grid.points.keys.toMutableSet()
//        dist[Point(0, 0)] = 0
//
//        while (q.isNotEmpty()) {
//            val u = q.minBy { dist[it]!! }
//            q.remove(u)
//
//            val last3 = previous.getLast3Steps(u)
//            val canGoHorizontal = last3.size < 3 || !last3.all { it.y == last3.first().y }
//            val canGoVertical = last3.size < 3 || !last3.all { it.x == last3.first().x }
//
//            listOfNotNull(
//                u.top.takeIf { canGoVertical },
//                u.left.takeIf { canGoHorizontal },
//                u.right.takeIf { canGoHorizontal },
//                u.bottom.takeIf { canGoVertical },
//            ).filter {
//                // Stop going backwards
//                grid[it] != Parser.Block.offGrid
//            }.also { points ->
////                println("u=$u h:$canGoHorizontal v:$canGoVertical dist: ${dist[u]} prev ${last3} ${points.map { "$it: ${grid[it].value} ${dist[it]}" }}")
//
//            }.forEach { neighbour ->
//                val alt = dist[u]!! + grid[neighbour].value
//                println("u: $u (dist=${dist[u]} | Considering $neighbour (dist=${dist[neighbour]}), alt: ${alt}")
//                if (alt < dist[neighbour]!!) {
//                    dist[neighbour] = alt
//                    previous[neighbour] = u
//                    println("Setting prev[$neighbour] = $u")
//                }
//            }
//        }
//
//        println(grid)
//        previous.getLast3Steps(grid.bottomRightMostPoint,100).forEach {
//            grid[it] = grid[it].copy(override = "#")
//        }
//        println(grid)
//        println(previous.getLast3Steps(grid.bottomRightMostPoint,100))
//
//        println(previous)
//        dist[grid.bottomRightMostPoint]!!
private fun MutableMap<Point, Point?>.getLast3Steps(u: Point, limit: Int = 3): List<Point> {
    val list = mutableListOf<Point>()
    var currentPoint: Point? = u
    while (list.size < limit && currentPoint != null) {
        list.add(currentPoint)
        currentPoint = this[currentPoint]
    }
    return list
}
