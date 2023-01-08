package day12

import day7.Day7Main.debug
import java.nio.file.Files
import java.nio.file.Paths
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

class Day12Main {

    data class Position(val x: Int, val y: Int)
    data class Link(val node: Node, val weight: Int = 1)
    data class Node(val position: Position, val elevation: Int, var distance: Int, var adjacentNodes: List<Link> = emptyList())

    private val allNodes: List<Node>

    lateinit var startNode: Node
    lateinit var endNode: Node
    val data = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/day12/data.txt")).let { raw ->
        val grid = Array(raw.size) { Array(raw.first().length) { Node(Position(0,0), 0, Int.MAX_VALUE - 10) } }

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

        allNodes = grid.flatten()

        grid.forEachIndexed { y, row ->
            row.forEachIndexed { x, node ->
                val adjacentPositions = adjacentNodes(x, y, row.size, grid.size)

                node.adjacentNodes = adjacentPositions.map { grid[it.y][it.x] }.filter { adjacentNode ->
                    adjacentNode.elevation < node.elevation || adjacentNode.elevation - node.elevation <= 1
                }.map { Link(it) }
            }
        }

        grid
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

    fun execute(): Int {

        val q = allNodes.toMutableList()
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

        return endNode.distance
    }

    fun run() = execute()
    @OptIn(ExperimentalTime::class)
    fun runPart2(): Int {
        val results = mutableMapOf<Position, Int>()

        allNodes.filter { it.elevation == 0 }.forEachIndexed { index, node ->
            allNodes.forEach { it.distance = Int.MAX_VALUE - 10 } // Reset the grid
            node.distance = 0

            startNode = node
            results[node.position] = execute()

            if (index % 50 == 0) {
                println("Index $index finished")
            }
        }

        return results.values.minOf { it }
    }

    private fun printGrid() {
        data.forEach { row ->
            row.forEach {
                print(it.distance.debug() + " ")
            }
            println()
        }
    }
}