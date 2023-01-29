package y2021.day12

data class Cave(val name: String, val isBig: Boolean, var adjacent: List<Cave>) {
    override fun toString() = "$name | ${adjacent.map { it.name }}"

}

data class Graph(val start: Cave, val end: Cave, val all: List<Cave>)

object Parser {

    fun parse(raw: List<String>): Graph {
        val map = mutableMapOf<String, MutableList<String>>()

        raw.forEach { line ->
            val split = line.split("-")
            map.getOrPut(split[0]) { mutableListOf() }.add(split[1])
            map.getOrPut(split[1]) { mutableListOf() }.add(split[0])
        }

        val caves = map.keys.map { Cave(it, it.toUpperCase() == it, mutableListOf()) }

        caves.forEach { cave ->
            cave.adjacent = map[cave.name]!!.map { adjacent -> caves.first { it.name == adjacent } }
        }

        return Graph(
            caves.first { it.name == "start" },
            caves.first { it.name == "end" },
            caves
        )
    }
}