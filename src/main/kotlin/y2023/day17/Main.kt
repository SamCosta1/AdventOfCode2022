package y2023.day17

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.MovementDirection
import utils.Point
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 102,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = false,
) : Puzzle {
    data class Node(val point: Point, val direction: MovementDirection) {
        companion object {
//            val comparator:
        }
    }

    override fun runPart1(data: List<String>, runMode: RunMode): Int {
        val start = Point(0, 0)
        val end = Point(data[0].length - 1, data.lastIndex)

        // Map of Point -> Energy value
        val rawValues = data.mapIndexed { y, row -> row.mapIndexed { x, _ -> Point(x, y) } }.flatten()
            .associateWith { data[it.y.toInt()][it.x.toInt()].toString().toInt() }

        val queue = mutableSetOf(
            Node(start, MovementDirection.East),
            Node(start, MovementDirection.South)
        )

        val dist = mutableMapOf(queue.first() to 0, queue.last() to 0)
        val steps = mutableMapOf(queue.first() to 0, queue.last() to 0)

        val prev = mutableMapOf<Node, Node>()

        while (queue.isNotEmpty()) {
            val distV = queue.minOf { node -> dist.getOrDefault(node, Int.MAX_VALUE) }
            val v = queue.filter { dist[it] == distV }.first()
            queue.remove(v)

            // Handle step in same direction
            val adjacentInSameDirection =
                v.point(v.direction).takeIf { steps[v]!! < 3 }.takeIf { rawValues.contains(it) }
            if (adjacentInSameDirection != null) {
                val newNode = Node(adjacentInSameDirection, v.direction)
                if (distV + rawValues[adjacentInSameDirection]!! < dist.getOrDefault(newNode, Int.MAX_VALUE)) {
                    dist[newNode] = distV + rawValues[adjacentInSameDirection]!!
                    steps[newNode] = steps[v]!! + 1
                    prev[newNode] = v
                    queue.add(newNode)
                }
            }

            // Handle steps in 90 degree directions
            val adjacentNormals = v.direction.normals.map {
                Node(v.point(it), it)
            }
            adjacentNormals.filter { rawValues.contains(it.point) }.forEach { u ->
                val value = rawValues[u.point]!!
                if (distV + value < dist.getOrDefault(u, Int.MAX_VALUE)) {
                    dist[u] = distV + value

                    queue.add(u)
                    steps[u] = 1
                    prev[u] = v
                }
            }
        }


        val chain = mutableListOf<Node>()
        val endPoints = prev.keys.filter { it.point == end }
        println("End points $endPoints")

        var current: Node? = endPoints.minBy { dist[it]!! }
        chain.add(current!!)
        while (current?.point != null) {
            current = prev[current]
            if (current != null) {
                chain.add(current)
            }
        }
        data.mapIndexed { y, row ->
            row.mapIndexed { x, c ->
                val chainNode = chain.firstOrNull { it.point == Point(x, y) }
                if (chainNode != null) {
                    print(chainNode.direction.char)
                } else {
                    print(c)
                }
            }
            print(' ')
            print(row)
            println()
        }
        println(chain)
        println(endPoints.map { dist[it] })
        return endPoints.minOf { dist[it]!! }
    }


    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}

fun Point.adjacent() = listOf(
    Point(x - 1, y),
    Point(x + 1, y),
    Point(x, y + 1),
    Point(x, y - 1)
)

//val all = data.mapIndexed { y, row -> row.mapIndexed { x, _ -> Point(x, y) } }.flatten()
//    .associateWith { data[it.y.toInt()][it.x.toInt()].toString().toInt() }
//val q = data.mapIndexed { y, row -> row.mapIndexed { x, _ -> Point(x, y) } }.flatten().toMutableSet()
//val s = mutableListOf<Point>()
//val prev = mutableMapOf<Point, Point>()
//
//while (q.isNotEmpty()) {
//    val v = q.minBy { dist.getOrDefault(it, Int.MAX_VALUE) }
//    q.remove(v)
//    val adjacent = v.adjacent().filter { all.contains(it) }
//    adjacent.forEach { u ->
//        val chain = mutableListOf(u, v)
//        val dimension = if (u.x == v.x) "X" else "Y"
//        repeat(3) {
//            prev[chain.last()]?.let {
//                if ((dimension == "X" && it.x == u.x) || (dimension == "Y" && it.y == u.y)) {
//                    chain.add(it)
//                }
//            }
//        }
//        var forward = prev.filterKeys { prev[it] == u }.keys
////                while (forward.isNotEmpty()) {
////
////                }
//
//        val isInvalidChain = chain.size > 4
//        if (!isInvalidChain && dist[v]!! + all[u]!! < dist.getOrDefault(u, Int.MAX_VALUE)) {
//            dist[u] = dist[v]!! + all[u]!!
//
//            prev[u] = v
//        }
//    }
//}
//val chain = mutableListOf(end)
//var current: Point? = end
//while (current != start) {
//    chain.add(prev[current]!!)
//    current = prev[current]
//}