package day23

import day15.Point
import java.nio.file.Files
import java.nio.file.Paths

object Parser {
    fun parse(file: String) = Grid().also { grid ->
        Files.readAllLines(
            Paths.get(
                System.getProperty("user.dir"),
                "src/main/kotlin/day23/$file"
            )
        ).forEachIndexed { y, row ->
            row.forEachIndexed { x, col ->
                if (col == '#') {
                    val position = Point(x.toLong(), y.toLong())
                    grid[position] = Grid.State.Elf(position)
                }
            }
        }
    }
}