package y2022.day24

import java.nio.file.Files
import java.nio.file.Paths

object Parser {
    fun parse(file: String): Grid {

        val lines = Files.readAllLines(
            Paths.get(
                System.getProperty("user.dir"),
                "src/main/kotlin/y2022/day24/$file"
            )
        )
        val grid = Grid(lines.first().length, lines.size)

        println()
        lines.forEachIndexed { y, row ->
            row.forEachIndexed { x, char ->
                val state = when (char) {
                    '#' -> Grid.State.Rock
                    '.' -> when(y) {
                        0 -> Grid.State.Entrance
                        lines.lastIndex -> Grid.State.Exit
                        else -> null
                    }
                    else -> Grid.State.Blizzard(Grid.Direction.values().first { char == it.char })
                }
                if (state == Grid.State.Entrance) {
                    grid.add(x, y-1, Grid.State.Rock) // Stops algorithm going out into space
                }

                if (state == Grid.State.Exit) {
                    grid.add(x, y+1, Grid.State.Rock) // Stops algorithm going out into space
                }

                if (state != null) {
                    grid.add(x, y, state)
                }
            }
        }

        return grid
    }
}
