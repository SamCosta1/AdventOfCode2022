package y2023.day25

import org.nd4j.linalg.factory.Nd4j
import puzzlerunners.yearMain
import java.nio.file.Files
import java.nio.file.Paths

object Parser {
    data class Info(val nodes: Map<String, MutableSet<String>>)

    fun parse(data: List<String>): Info {
        val nodes = data.map { row ->
            row.split(":").map {
                it.split(" ")
            }.flatten()
        }.flatten().filter { it.isNotBlank() }.map { it.trim() }.toSet().toList()

        val adjacencyMatrix = Array(nodes.size) { Array(nodes.size) { 0 } }
        data.forEach { row ->
            val node1 = row.split(":").first()
            val node1Index = node1.let { nodes.indexOf(it) }
            val otherNodes = row.split(":").last().trim().split(" ")

            otherNodes
                .map { nodes.indexOf(it) }
                .forEach { node2 ->
                    adjacencyMatrix[node1Index][node2] = 1
                    adjacencyMatrix[node2][node1Index] = 1
                }
        }

        val nodeMap = nodes.associateWith {  thisNode ->
            val indexThis = nodes.indexOf(thisNode)
            nodes.filter { otherNode -> adjacencyMatrix[indexThis][nodes.indexOf(otherNode)] == 1 }.toMutableSet()
        }

        return Info(nodeMap)
    }

    private fun Array<Array<Int>>.matrixString() = buildString {
        forEach { array: Array<Int> ->
            appendLine(array.joinToString())
        }
    }
}