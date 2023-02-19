package y2021.day15

import utils.Puzzle
import utils.RunMode
import utils.Point
import y2022.day16.Room
import kotlin.math.max

class Main : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        runAlgo(grid)
    }

    private fun runAlgo(grid: Grid): Long {
        val startPoint = Point(0, 0)
        val dist = grid.points.mapValues { if (it.key == startPoint) 0L else Long.MAX_VALUE - 1 }.toMutableMap()
        val q = grid.points.keys.toMutableList()

        while (q.isNotEmpty()) {
            val next = q.minBy { dist[it]!! }!!

            q.remove(next)

            grid.adjacentNodes(next.x, next.y).forEach {
                if (dist[next]!! + grid.points[it]!! < dist[it]!!) {
                    dist[it] = dist[next]!! + grid.points[it]!!
                }
            }
        }

        return dist[Point(grid.width - 1L, grid.height - 1L)]!!
    }

    val repeats = 5
    override fun runPart2(data: List<String>, runMode: RunMode) = Grid(data.first().length * repeats, data.size * repeats).apply {
        repeat(repeats) { yRepeat ->
            data.forEachIndexed { y, row ->
                repeat(repeats) { xRepeat ->
                    row.forEachIndexed { x, char ->
                        val point = Point(
                            x + xRepeat.toLong() * data.size,
                            y + yRepeat.toLong() * data.size
                        )
                        val i = char.toString().toInt() + yRepeat + xRepeat
                        val new = i % 10 + (i / 10)
                        points[point] = if (new == 10) 1 else new
                    }
                }
            }
        }

//        println("s")
//        var a = 0
//        (0..62500000_000).forEach { a++ }
//        println("b")

    }.let { grid ->
        runAlgo(grid)
    }
}
