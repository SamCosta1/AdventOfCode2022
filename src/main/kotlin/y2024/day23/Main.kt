package y2024.day23

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 7,
    override val part2ExpectedAnswerForSample: Any = "co,de,ka,ta",
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(
        data: List<String>,
        runMode: RunMode
    ) = data.map { row -> row.split("-") }.let { pairs ->
        val computers = pairs.flatten().toSet()

        return@let part1ExpectedAnswerForSample
        val results = mutableSetOf<Set<String>>()
        pairs.forEachIndexed { index, (p1, p2) ->
            computers.filter { thirdComputer ->
                thirdComputer != p1
                        && thirdComputer != p2
                        && pairs.any { it.contains(thirdComputer) && it.contains(p1) }
                        && pairs.any { it.contains(thirdComputer) && it.contains(p2) }
            }.forEach { results.add(setOf(p1, p2, it)) }
            println("Done $index / ${pairs.size}")
        }
        results.filter { it.any { it.startsWith('t') } }.also { println(it.joinToString("\n")) }.size

    }

    override fun runPart2(data: List<String>, runMode: RunMode) = data.map { row -> row.split("-") }.let { pairs ->
        val nodes = pairs.flatten().toSet().let { set -> set.associateWith { set.indexOf(it) } }
        val nodesByIndex = nodes.entries.associate { (k, v) -> v to k }
        val adjMatrix = Array(nodes.size) { Array(nodes.size) { 0 } }

        pairs.forEach { (p1, p2) ->
            adjMatrix[nodes[p1]!!][nodes[p2]!!] = 1
            adjMatrix[nodes[p2]!!][nodes[p1]!!] = 1
        }

//        println(adjMatrix.map { it.joinToString("") }.joinToString("\n"))
//        val maxK = adjMatrix.maxOf { it.sum() }
//        println("Max k $maxK")
//        var kTarget = maxK + 4
//        while (kTarget > 1) {
            val kSubGraphOrNull = findKSubgraph(0, nodesByIndex.keys, adjMatrix)
//            if (kSubGraphOrNull != null) {
                return@let kSubGraphOrNull!!.map { nodesByIndex[it]!! }.sorted().joinToString(",")
//            }
//            kTarget--
//        }
//        throw Exception("Failed to find subgraph")
    }

    // af,ej,fa,ij,jw,ob,oh,ql,qu,vl,vq,xf

    data class Node(val raw: Int)

    private fun findKSubgraph(kTarget: Int, nodes: Set<Int>, adjMatrix: Array<Array<Int>>): Collection<Int> {
//        val nodesInConsideration = nodes.map { Node(it) }.toMutableSet()
//
//        fun degree(node: Node): Int {
//            val edges = adjMatrix[node.raw]
//            var result = 0
//            edges.forEachIndexed { colIndex, i ->
//                if (nodesInConsideration.contains(Node(colIndex))) {
//                    result+=i
//                }
//            }
//            return result
//        }

        var largestSoFar: Collection<Int> = emptySet()
        //        algorithm BronKerbosch2(R, P, X) is
        fun bronKerbosch(r: Set<Int>, p: MutableSet<Int>, x: MutableSet<Int>) {

            if (p.isEmpty() && x.isEmpty()) {
                if (r.size > largestSoFar.size) {
                    largestSoFar = r
                }
                return
            }

            while(p.isNotEmpty()) {
                val v = p.first()
                val neighbours = adjMatrix[v].indices.filter { adjMatrix[v][it] == 1 }
                val bk = bronKerbosch(
                    r + v,
                    p.intersect(neighbours.toSet()).toMutableSet(),
                    x.intersect(neighbours.toSet()).toMutableSet()
                )

                p.remove(v)
                x.add(v)
            }
        }


        bronKerbosch(emptySet(), nodes.toMutableSet(), mutableSetOf())
        return largestSoFar
    }
}