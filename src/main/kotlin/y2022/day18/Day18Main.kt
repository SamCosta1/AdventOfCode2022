package y2022.day18

import java.lang.IllegalStateException

class Day18Main(file: String) {

    val data = Parser.parse(file).toSet()

    private fun calculateSurface(cubes: Collection<Cube>): Int {
        val set = cubes.toSet()

        return cubes.sumOf { cube ->
            cube.adjacentCubes.count { !set.contains(it) }
        }
    }

    fun run(): Int = calculateSurface(data)

    val minX = data.minOf { it.x } - 20
    val maxX = data.maxOf { it.x } + 20

    val minY = data.minOf { it.y } - 20
    val maxY = data.maxOf { it.y } + 20

    val minZ = data.minOf { it.z } - 20
    val maxZ = data.maxOf { it.z } + 20

    fun runPart2(): Int {


        println("minX=$minX, mxX=$maxX, minY=$minY maxY=$maxY minZ=$minZ maxZ=$maxZ cubeSetSize: ${data.size}")

        val outerAir = mutableSetOf<Cube>()

        val searchStart = Cube(minX, minY, minZ)
        if (data.contains(searchStart)) {
            throw IllegalStateException("Cube shouldn't be here")
        }

        buildOuterAirSet(searchStart, outerAir)

        println("Outer air: " + outerAir.size)


        return data.sumBy { cube ->
            cube.adjacentCubes.count {
                outerAir.contains(it)
            }
        }
    }

    var count = 0
    fun buildOuterAirSet(searchNode: Cube, outerAir: MutableSet<Cube>) {

        val nodesToCheck = mutableSetOf<Cube>(searchNode)

        while (nodesToCheck.isNotEmpty()) {
            val iterator = nodesToCheck.iterator()
            val node = iterator.next()

            outerAir.add(node)
            iterator.remove()
            nodesToCheck.addAll(node.adjacentCubes.filter {
                it.isInBoundingBox() && !outerAir.contains(it) && !data.contains(it)
            })
        }
    }

    fun Cube.isInBoundingBox() = x in minX..maxX && y in minY..maxY && z in minZ..maxZ
}