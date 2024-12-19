package y2024.day16

import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.MovementDirection
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 11048,
    override val part2ExpectedAnswerForSample: Any = 64,
    override val isComplete: Boolean = true
) : Puzzle {

    // 123541 to high
    override fun runPart1(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        findShortestPathDistance(grid).shortestPath
    }

    data class Answer(val shortestPath: Int, val numOnShortestPath: Int)
    private fun findShortestPathDistance(grid: GenericGrid<Parser.Item>): Answer  {
        val start = grid.points.filter { it.value == Parser.Item.Start }.keys.first()
        val end = grid.points.filter { it.value == Parser.Item.End }.keys.first()
        grid[start] = Parser.Item.Free
        grid[end] = Parser.Item.Free

        val queue = mutableSetOf(
            start to MovementDirection.East
        )
        val dist = mutableMapOf(queue.first() to 0)
        val prev = mutableMapOf<Pair<Point, MovementDirection>, MutableSet<Pair<Point, MovementDirection>>>()

        while (queue.isNotEmpty()) {
            val distV = queue.minOf { dist.getOrDefault(it, Int.MAX_VALUE) }
            val (vPoint, vDir) = queue.first { dist[it] == distV }.also { queue.remove(it) }

            // Step forward
            if (grid[vPoint(vDir)] == Parser.Item.Free) {
                val newPair = vPoint(vDir) to vDir
                if (distV + 1 == dist[newPair]) {
                    prev.getOrPut(newPair, { mutableSetOf() }).add(vPoint to vDir)
                }
                if (distV + 1 < dist.getOrDefault(newPair, Int.MAX_VALUE)) {
                    dist[newPair] = distV + 1
                    queue.add(newPair)
                    prev[newPair] = mutableSetOf(vPoint to vDir)
                }
            }

            // Turn
            listOf(vDir.turnLeft90Degrees, vDir.turnRight90Degrees).forEach { newDir ->
                if (grid[vPoint(newDir)] == Parser.Item.Free) {
                    val newPair = vPoint(newDir) to newDir
                    if (distV + 1001 == dist[newPair]) {
                        prev.getOrPut(newPair) { mutableSetOf() }.add(vPoint to vDir)
                    }
                    if (distV + 1001 < dist.getOrDefault(newPair, Int.MAX_VALUE)) {
                        dist[newPair] = distV + 1001
                        queue.add(newPair)
                        prev[newPair] = mutableSetOf(vPoint to vDir)
                    }
                }
            }
        }

        val shortestDistance = dist.filter { it.key.first == end }.minOf { it.value }
        val minPathEnds = dist.filter { it.key.first == end && it.value == shortestDistance }

        val allInPaths = minPathEnds.keys.toMutableSet()
        var lastPoints = prev.filter { minPathEnds.contains(it.key)  }.values.flatten()

        while (lastPoints.isNotEmpty()) {
            val newLast = prev.filter { lastPoints.contains(it.key) }.values.flatten()
            allInPaths.addAll(lastPoints.map { it })
            lastPoints = newLast
        }
        allInPaths.map { it.first }.forEach {
            grid[it] = Parser.Item.Start
        }

        return Answer(shortestDistance, allInPaths.map { it.first }.toSet().size)
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        findShortestPathDistance(grid).numOnShortestPath
    }
}
