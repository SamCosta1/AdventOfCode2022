package y2021.day15

import puzzlerunners.Puzzle
import utils.RunMode
import utils.Point

class Main(override val part1ExpectedAnswerForSample: Any, override val part2ExpectedAnswerForSample: Any) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        runAlgo(grid)
    }

    private fun runAlgo(grid: Grid): Long {
        val startPoint = Point(0, 0)
        val end = Point(grid.width - 1L, grid.height - 1L)

        val dist = grid.points.mapValues { if (it.key == startPoint) 0L else Long.MAX_VALUE - 1 }.toMutableMap()
        val q = grid.points.keys.toMutableList()

        while (q.isNotEmpty()) {
            val current = q.minBy { dist[it]!! }!!

            q.remove(current)

            grid.adjacentNodes(current.x, current.y).forEach {
                if (dist[current]!! + grid.points[it]!! < dist[it]!!) {
                    dist[it] = dist[current]!! + grid.points[it]!!

                }
            }
        }


        return dist[end]!!
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

    }.let { grid ->
        runAlgo(grid)
    }
}
