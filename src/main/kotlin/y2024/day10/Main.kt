package y2024.day10

import puzzlerunners.Puzzle
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 36,
    override val part2ExpectedAnswerForSample: Any = 81,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val zeros = grid.points.filter { it.value.value == 0 }.keys

        var result = 0
        zeros.forEach { startPos ->

            val dist = mutableMapOf(startPos to 0)
            val q = grid.points.keys.toMutableSet()

            while (q.isNotEmpty()) {
                val v = q.minBy { dist.getOrDefault(it, Int.MAX_VALUE) }
                if (dist[v] == null) {
                    break
                }
                val vValue = grid[v].value
                val alt = dist[v]!! + 1
                q.remove(v)
                v.adjacentNoDiagonal().asSequence().filter {
                    grid[it].value != -1
                }.filter {
                    grid[it].value == vValue + 1
                }.forEach { u ->
                    if (alt < dist.getOrDefault(u, Int.MAX_VALUE)) {
                        dist[u] = alt
                    }
                }
            }

            result += dist.count { grid[it.key].value == 9 }
        }

        result
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val zeros = grid.points.filter { it.value.value == 0 }

        var total = 0
        fun follow(node: Point) {
            if (grid[node].value == 9) {
                total++
                return
            }
            var nextSteps = node.adjacentNoDiagonal().filter {
                grid[it].value == grid[node].value + 1
            }

            while (nextSteps.size == 1 && grid[nextSteps[0]].value != 9) {
                nextSteps = nextSteps[0].adjacentNoDiagonal().filter {
                    grid[it].value == grid[nextSteps[0]].value + 1
                }
            }

            nextSteps.forEach { point -> follow(point) }
        }

        zeros.forEach { follow(it.key) }
        total
    }
}
