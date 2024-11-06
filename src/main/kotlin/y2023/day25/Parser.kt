package y2023.day25

import org.nd4j.linalg.factory.Nd4j
import puzzlerunners.yearMain
import java.nio.file.Files
import java.nio.file.Paths

object Parser {
    fun parse(data: List<String>): Pair<List<String>, Array<Array<Int>>> {
        val nodes = data.map { row -> row.split(":").map {
            it.split(" ")
        }.flatten() }.flatten().filter { it.isNotBlank() }.map { it.trim() }.toSet().toList()

        val adjacencyMatrix = Array(nodes.size) { Array(nodes.size) { 0 } }

        data.forEach { row ->
            val node1 = row.split(":").first().let { nodes.indexOf(it) }
            val otherNodes = row.split(":").last().trim().split(" ").map {
                nodes.indexOf(it)
            }

            otherNodes.forEach { node2 ->
                adjacencyMatrix[node1][node2] =  1
                adjacencyMatrix[node2][node1] =  1
            }
        }

        return Pair(nodes, adjacencyMatrix)
    }

    private fun Array<Array<Int>>.matrixString() = buildString {
        forEach { array: Array<Int> ->
            appendLine(array.joinToString())
        }
    }
}