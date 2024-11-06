package y2023.day17

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.MovementDirection
import utils.Point
import utils.RunMode
import java.util.PriorityQueue

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
        val all = data.mapIndexed { y, row -> row.mapIndexed { x, _ -> Point(x, y) } }.flatten()
            .associateWith { data[it.y.toInt()][it.x.toInt()].toString().toInt() }

        val q = mutableSetOf(
            Node(start, MovementDirection.East),
            Node(start, MovementDirection.South)
        )

        val dist = mutableMapOf(start to 0, start to 0)
        val steps = mutableMapOf(q.first() to 1, q.last() to 1)

        val prev = mutableMapOf<Node, Node>()
        var count = 0
        while (q.isNotEmpty()) {
            val v = q.minBy { node -> dist.getOrDefault(node.point, Int.MAX_VALUE) }
            q.remove(v)

            val distV = dist[v.point]!!
            val adjacentNormals = v.direction.normals.map {
                Node(v.point(it), it)
            }
            val adjacentInChain = v.point(v.direction).takeIf { steps[v]!! < 3 }.takeIf { all.contains(it) }

            if (adjacentInChain != null) {
//                println("distv=$distV all[adjacentInChain]!!=${all[adjacentInChain]!!}")
                if (distV + all[adjacentInChain]!! < dist.getOrDefault(adjacentInChain, Int.MAX_VALUE)) {
                    dist[adjacentInChain] = distV + all[adjacentInChain]!!
                    val newNode = Node(adjacentInChain, v.direction)
                    steps[newNode] = steps[v]!! + 1
                    prev[newNode] = v
                    q.add(newNode)
                }
            }

//            println("v=$v adjacentInChain=$adjacentInChain adjacentNormals=${adjacentNormals.filter { all.contains(it.point) }}")
            adjacentNormals.filter { all.contains(it.point) }.forEach { u ->
                val value = all[u.point]!!

                if (distV + value < dist.getOrDefault(u.point, Int.MAX_VALUE)) {
                    dist[u.point] = distV + value

                    q.add(u)
                    steps[u] = 1
                    prev[u] = v
                }
            }
        }


        val chain = mutableListOf<Node>()
        var current: Node? = prev.keys.filter { it.point == end }.first()
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
        return dist[end]!!
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