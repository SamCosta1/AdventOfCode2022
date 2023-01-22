package utils

import java.nio.file.Files
import java.nio.file.Paths

object NewYearCreator {

    fun run(year: Int) {

        (1..25).forEach {
            Files.write(
                Paths.get(
                    System.getProperty("user.dir"),
                    "src/main/kotlin/y$year/day$it/Main.kt"
                ),
                """
package y2021.day$it

import utils.Puzzle
import utils.RunMode

class Main: Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = ""
    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
            """.trimIndent().split("\n")
            )

            Files.write(
                Paths.get(
                    System.getProperty("user.dir"),
                    "src/main/kotlin/y$year/day$it/data.txt"
                ),
                emptyList()
            )
            Files.write(
                Paths.get(
                    System.getProperty("user.dir"),
                    "src/main/kotlin/y$year/day$it/sample.txt"
                ),
                emptyList()
            )
        }
    }
}