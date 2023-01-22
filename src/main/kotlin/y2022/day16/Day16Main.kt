package y2022.day16

import kotlin.math.max


class Day16Main(file: String, private val totalTime: Int) {

    val data = Parser.parse(file)
    val startRoom: Room by lazy { data.first { it.name == "AA" } }

    data class StatefulRoom(val room: Room, var isOn: Boolean = false)

    data class PathAnalysisResult(val totalFlow: Long, val timeTaken: Int)

    fun List<StatefulRoom>.totalFlow(debug: Boolean = false): PathAnalysisResult {
        var total = 0L
        var minsRemaining = totalTime

        forEach { statefulRoom ->
            if (statefulRoom.isOn) {
                minsRemaining--
                total += (minsRemaining) * statefulRoom.room.flowRate
                minsRemaining--
            } else {
                minsRemaining--
            }
        }
        return PathAnalysisResult(total, totalTime - minsRemaining)
    }

    var bestSol = 0L
    var bestPath = emptyList<Room>()
    fun execute(rooms: MutableList<Room>, fullPathSoFar: List<StatefulRoom>, currentIndex: Int) {
        for (i in currentIndex until rooms.size) {
            val r = rooms[i]
            rooms[i] = rooms[currentIndex]
            rooms[currentIndex] = r

            val newPath = fullPathSoFar +
                    route(fullPathSoFar.last().room, rooms[currentIndex]).map { StatefulRoom(it) } +
                    StatefulRoom(rooms[currentIndex], true)

            val solutionAnalysis = newPath.totalFlow()

            if (solutionAnalysis.totalFlow > bestSol) {
                bestSol = solutionAnalysis.totalFlow
                bestPath = newPath.map { it.room }
            }
            if (solutionAnalysis.timeTaken < totalTime - 1) {
                execute(rooms, newPath, currentIndex + 1)
            }

            val r1 = rooms[currentIndex]
            rooms[currentIndex] = rooms[i]
            rooms[i] = r1
        }
    }

    fun run(): String {
        val openable = data.filter { it.isOpenable }

        execute(openable.toMutableList(), listOf(StatefulRoom(startRoom, false)), 0)

        return bestSol.toString()
    }

    val djikstraCache = mutableMapOf<String, Map<String, Room>>() // startNode -> map of previous values stored
    fun route(from: Room, toRoom: Room): List<Room> {
        val arrOfPrevious = djikstraCache.getOrPut(from.name) { djikstra(from) }

        val path = mutableListOf<Room>()

        var current: Room? = arrOfPrevious[toRoom.name]
        while (current != null && current != from) {
            path += current
            current = arrOfPrevious[current.name]
        }
        return path.reversed()
    }

    var djikstra = 0
    fun djikstra(from: Room): Map<String, Room> {
        djikstra++
        val q = data.map { it }.toMutableList()
        val previous = mutableMapOf<String, Room>()

        val distances = q.associate { it.name to Int.MAX_VALUE - 10 }.toMutableMap()
        distances[from.name] = 0

        while (q.isNotEmpty()) {
            val next = q.minByOrNull {
                distances[it.name] ?: throw NullPointerException("distances[${it.name}] is null}")
            } ?: throw java.lang.NullPointerException("Can't get min values $q")

            q.remove(next)

            next.adjacentNodes.forEach { u ->
                if (distances[next.name]!! + 1 < distances[u.name]!!) {
                    distances[u.name] = distances[next.name]!! + 1
                    previous[u.name] = next
                }
            }
        }


        return previous
    }

    var count = 0
    fun runPart2(): String {
        val openable = data.filter { it.isOpenable }
        val lookupMap = data.associateBy { it.name }.toMap()

        val openableNames = openable.map { it.name }

        var part2BestSol = 0L
        (1..openable.size/2).forEach { size ->
            Utils.getSubsets(openableNames, size).forEach {  set ->
                val thisSet = set.toList().map { lookupMap[it]!! }
                val otherSet = openable.filter { o -> !set.contains(o.name) }
                count++

                var thisSol = 0L
                bestSol = 0
                execute(thisSet.toMutableList(), listOf(StatefulRoom(startRoom, false)), 0)
                thisSol += bestSol

                bestSol = 0
                execute(otherSet.toMutableList(), listOf(StatefulRoom(startRoom, false)), 0)
                thisSol+=bestSol

                part2BestSol = max(part2BestSol, thisSol)

//                if (count % 2000 == 0) {
//                    println("${(count * 100) / 16383}%" )
//                }
            }
        }


        return part2BestSol.toString()
    }
}