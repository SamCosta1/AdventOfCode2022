package y2022.day12

import puzzlerunners.Puzzle
import utils.RunMode
import kotlin.time.ExperimentalTime

class Main(
    override val part1ExpectedAnswerForSample: Any = 31,
    override val part2ExpectedAnswerForSample: Any = 29,
    override val isComplete: Boolean = true
) : Puzzle {
    data class Position(val x: Int, val y: Int)
    data class Link(val node: Node, val weight: Int = 1)

    data class Info(val allNodes: List<Node>, val grid: Array<Array<Node>>, val startNode: Node, val endNode: Node)
    data class Node(val position: Position, val elevation: Int, var distance: Int, var adjacentNodes: List<Link> = emptyList())

    fun parse(raw: List<String>): Info {
        val grid = Array(raw.size) { Array(raw.first().length) { Node(Position(0,0), 0, Int.MAX_VALUE - 10) } }


        var startNode: Node? = null
        var endNode: Node? = null

        var start: Position? = null
        var end: Position? = null
        raw.forEachIndexed { y, row ->
            row.toCharArray().map { it.toString() }.forEachIndexed { x, elevation ->
                when (elevation) {
                    "S" -> {
                        start = Position(x, y)
                        grid[y][x] = Node(start!!, "a".toElevation(), 0).also {
                            startNode = it
                        }
                    }
                    "E" -> {
                        end = Position(x, y)
                        grid[y][x] = Node(end!!, "z".toElevation(), Int.MAX_VALUE - 10).also {
                            endNode = it
                        }
                    }
                    else -> {
                        grid[y][x] = Node(Position(x, y), elevation.toElevation(), Int.MAX_VALUE - 10)
                    }
                }
            }
        }

        val allNodes = grid.flatten()

        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, node ->
                val adjacentPositions = adjacentNodes(x, y, row.size, grid.size)

                node.adjacentNodes = adjacentPositions.map { grid[it.y][it.x] }.filter { adjacentNode ->
                    adjacentNode.elevation < node.elevation || adjacentNode.elevation - node.elevation <= 1
                }.map { Link(it) }
            }
        }

        return Info(allNodes, grid, startNode!!, endNode!!)
    }

    fun adjacentNodes(x: Int, y: Int, width: Int, height: Int) = listOf(
//        Position(x - 1, y - 1),
        Position(x - 1, y ),
//        Position(x -1, y + 1),
        Position(x, y + 1),
//        Position(x + 1, y + 1),
        Position(x + 1, y),
//        Position(x + 1, y - 1),
        Position(x, y - 1),
    ).filter { (it.x in 0 until width) && (it.y in 0 until height)  }

    fun String.toElevation() = "abcdefghijklmnopqrstuvwxyz".indexOf(this)

    fun execute(info: Info): Int {

        val q = info.allNodes.toMutableList()
        val s = mutableMapOf<Position, Node>()

        while (q.isNotEmpty()) {
            val next = q.filter { qItem ->
                !s.containsKey(qItem.position)
            }.minByOrNull { it.distance }!!

            q.remove(next)
            s[next.position] = next

            next.adjacentNodes.forEach { u ->
                if (next.distance + u.weight < u.node.distance) {
                    u.node.distance = next.distance + u.weight
                }
            }
        }

        return info.endNode.distance
    }

    override fun runPart1(data: List<String>, runMode: RunMode) = execute(parse(data))

    override fun runPart2(data: List<String>, runMode: RunMode): Any {
        val info = parse(data)
        val results = mutableMapOf<Position, Int>()

        info.allNodes.filter { it.elevation == 0 }.forEachIndexed { index, node ->
            info.allNodes.forEach { it.distance = Int.MAX_VALUE - 10 } // Reset the grid
            node.distance = 0

//             Slow as balls so commented for now
            results[node.position] = 2//execute(info.copy(startNode = node))
        }

        return results.values.minOf { it }
    }
}