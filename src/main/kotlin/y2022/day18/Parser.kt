package y2022.day18

import java.nio.file.Files
import java.nio.file.Paths

data class Cube(val x: Long, val y: Long, val z: Long) {
    val adjacentCubes get() = listOf(
        Cube(x - 1, y, z), // left
        Cube(x + 1, y, z), // right

        Cube(x, y + 1, z), // top
        Cube(x, y - 1, z), // bottom

        Cube(x, y, z + 1), // in front
        Cube(x, y, z - 1), // bottom
    )

    fun isAdjacent(otherCube: Cube) = adjacentCubes.contains(otherCube)
}

object Parser {
    fun parse(file: String) = Files.readAllLines(
        Paths.get(
            System.getProperty("user.dir"),
            "src/main/kotlin/y2022/day18/$file"
        )
    ).map { raw ->
        val split = raw.split(",")
        Cube(split[0].toLong(), split[1].toLong(), split[2].toLong())
    }
}