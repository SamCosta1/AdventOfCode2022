package y2024.day16

import org.nd4j.linalg.indexing.conditions.Not
import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.GenericGrid
import utils.MovementDirection
import utils.Point
import utils.RunMode
import kotlin.math.max
import kotlin.math.min

class Main(
    override val part1ExpectedAnswerForSample: Any = 11048,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
) : Puzzle {

    // 123541 to high
    override fun runPart1(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        val start = grid.points.filter { it.value == Parser.Item.Start }.keys.first()
        val end = grid.points.filter { it.value == Parser.Item.End }.keys.first()
        findShortestPathDistance(start, end, grid, runMode)
    }
// 670 too high
    private fun findShortestPathDistance(start: Point, end: Point, grid: GenericGrid<Parser.Item>, runMode: RunMode): Int {
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

        // This answer is one higher than the real input, I have no idea why
        val answerIsh = dist.filter { it.key.first == end }.minOf { it.value }

        val minPathEnds = dist.filter { it.key.first == end && it.value == answerIsh }

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

        return answerIsh
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { grid ->
        val start = grid.points.filter { it.value == Parser.Item.Start }.keys.first()
        val end = grid.points.filter { it.value == Parser.Item.End }.keys.first()
        val shortestPath = findShortestPathDistance(start, end, grid, runMode)

        val allTiles = mutableSetOf<Point>()
        fun go(current: Point, direction: MovementDirection, scoreSoFar: Int, soFar: Set<Pair<Point, MovementDirection>>) {
            if (scoreSoFar > shortestPath) {
                return
            }

            val shortestLeft = (end - current).let { min(it.x, -it.y) }
            val stepsRemaining = shortestPath - scoreSoFar
            if (stepsRemaining < shortestLeft) {
                return
            }

            if (current == end) {
                allTiles.addAll(soFar.map { it.first })
                return
            }
            sequenceOf(
                direction,
                direction.turnRight90Degrees,
                direction.turnLeft90Degrees
            ).filter { grid[current(it)] == Parser.Item.Free}.forEach { newDir ->
                val nextPoint = current(newDir)
                if (!soFar.contains(nextPoint to newDir)) {
                    go(
                        nextPoint,
                        newDir,
                        scoreSoFar + if (newDir == direction) 1 else 1001,
                        soFar + (nextPoint to newDir)
                    )
                }
            }
        }

//        go(current = start, MovementDirection.East, 0, setOf(start to MovementDirection.East))
        println(allTiles.size)
        allTiles.size
    }
}
