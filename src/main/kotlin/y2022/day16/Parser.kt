package y2022.day16

import java.nio.file.Files
import java.nio.file.Paths

data class Room(
    val name: String,
    val flowRate: Long,
    val adjacentNodes: MutableList<Room>,
) {
    override fun toString(): String {
        return "$name | rate=$flowRate | links: ${adjacentNodes.map { it.name }}"
    }

    val isOpenable get() = flowRate > 0
}

object Parser {

    private data class RawRoom(val name: String, val flowRate: Long, val adjacentNodes: List<String>)

    fun parse(file: String) = Files.readAllLines(
        Paths.get(
            System.getProperty("user.dir"),
            "src/main/kotlin/y2022/day16/$file"
        )
    ).map { rawLine ->
        val groups = "Valve (.*) has flow rate=(\\d+); tunnels? leads? to valves? (.*)".toRegex().matchEntire(
            rawLine
        )!!.groupValues.drop(1)
        RawRoom(groups.first(), groups[1].toLong(), groups.last().split(", "))
    }.let { rawRooms ->
        val rooms = rawRooms.map { Room(name = it.name, it.flowRate, mutableListOf()) }
        rawRooms.forEachIndexed { index, rawRoom ->
            rooms[index].adjacentNodes.addAll(rawRoom.adjacentNodes.map { rawAdjacentNode ->
                rooms.find { it.name == rawAdjacentNode }!!
            })
        }
        rooms
    }
}