package y2024.day23

import puzzlerunners.Puzzle
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
        val nodes = pairs.flatten().toSet().let { set -> set.associateWith { set.indexOf(it) } }
        val adjMatrix = Array(nodes.size) { Array(nodes.size) { 0 } }

        pairs.forEach { (p1, p2) ->
            adjMatrix[nodes[p1]!!][nodes[p2]!!] = 1
            adjMatrix[nodes[p2]!!][nodes[p1]!!] = 1
        }

        val results = mutableSetOf<Set<String>>()
        pairs.forEachIndexed { index, (p1, p2) ->
            computers.filter { thirdComputer ->
                thirdComputer != p1
                        && thirdComputer != p2
                        && adjMatrix[nodes[p1]!!][nodes[thirdComputer]!!] == 1
                        && adjMatrix[nodes[p2]!!][nodes[thirdComputer]!!] == 1
            }.forEach { results.add(setOf(p1, p2, it)) }
        }
        results.filter { it.any { it.startsWith('t') } }.size
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = data.map { row -> row.split("-") }.let { pairs ->
        val nodes = pairs.flatten().toSet().let { set -> set.associateWith { set.indexOf(it) } }
        val nodesByIndex = nodes.entries.associate { (k, v) -> v to k }
        val adjMatrix = Array(nodes.size) { Array(nodes.size) { 0 } }

        pairs.forEach { (p1, p2) ->
            adjMatrix[nodes[p1]!!][nodes[p2]!!] = 1
            adjMatrix[nodes[p2]!!][nodes[p1]!!] = 1
        }

        var largestSoFar: Collection<Int> = emptySet()
        bronKerbosch(emptySet(), nodes.values.toMutableSet(), mutableSetOf(), adjMatrix) { result ->
            if (result.size > largestSoFar.size) {
                largestSoFar = result
            }
        }
        return@let largestSoFar.map { nodesByIndex[it]!! }.sorted().joinToString(",")
    }

    private fun bronKerbosch(
        r: Set<Int>,
        p: MutableSet<Int>,
        x: MutableSet<Int>,
        adjMatrix: Array<Array<Int>>,
        onResult: (Collection<Int>) -> Unit
    ) {
        if (p.isEmpty() && x.isEmpty()) {
            onResult(r)
            return
        }

        while (p.isNotEmpty()) {
            val v = p.first()
            val neighbours = adjMatrix[v].indices.filter { adjMatrix[v][it] == 1 }
            bronKerbosch(
                r + v,
                p.intersect(neighbours.toSet()).toMutableSet(),
                x.intersect(neighbours.toSet()).toMutableSet(),
                adjMatrix,
                onResult
            )

            p.remove(v)
            x.add(v)
        }
        return
    }

}