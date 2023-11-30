package y2021.day12

import utils.Puzzle
import utils.RunMode

typealias Route = List<Cave>
class Main(override val part1ExpectedAnswerForSample: Any, override val part2ExpectedAnswerForSample: Any) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { graph ->
        findAllPathsP1(graph.start, graph.end, listOf(graph.start)).size
    }

    private fun findAllPathsP1(source: Cave, destination: Cave, pathSoFar: List<Cave>): List<Route> {
        if (source == destination) {
            return listOf(pathSoFar)
        }

        return source.adjacent.filter { it.isBig || !pathSoFar.contains(it) }.map {
            findAllPathsP1(it, destination, pathSoFar + it)
        }.flatten()
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { graph ->
        val allowedTwice = graph.all.filter { !it.isBig && it != graph.start && it != graph.end } + null

        allowedTwice.map {
            findAllPathsP2(graph.start, graph.end, it, listOf(graph.start))
        }.flatten().map { route ->
            route.map { it.name}.joinToString("-")
        }.toSet().also {
//            it.forEach {  println(it) }
        }.size
    }

    private fun findAllPathsP2(source: Cave, destination: Cave, allowedTwice: Cave?, pathSoFar: List<Cave>): List<Route> {
        if (source == destination) {
            return listOf(pathSoFar)
        }

        return source.adjacent.filter { adjacent ->
            adjacent.isBig || (adjacent == allowedTwice && pathSoFar.count { it == allowedTwice } < 2) || !pathSoFar.contains(adjacent)
        }.map {
            findAllPathsP2(it, destination, allowedTwice,pathSoFar + it)
        }.flatten()
    }
}
