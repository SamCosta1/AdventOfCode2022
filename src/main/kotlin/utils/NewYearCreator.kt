package utils

import java.nio.file.Files
import java.nio.file.Paths
import java.time.Year

object NewYearCreator {

    fun run(year: Int) {
        (1..25).forEach {
            Files.createDirectories(
                Paths.get(
                    System.getProperty("user.dir"),
                    "src/main/kotlin/y$year/day$it"
                ),
            )
            Files.write(
                Paths.get(
                    System.getProperty("user.dir"),
                    "src/main/kotlin/y$year/day$it/Main.kt"
                ),
                """
package y$year.day$it

import utils.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
): Puzzle {
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
            Files.write(
                Paths.get(
                    System.getProperty("user.dir"),
                    "src/main/kotlin/y$year/Main2023.kt"
                ),
                yearMain(year).lines()
            )
        }
    }
}

fun yearMain(year: Int) = buildString {
    append("""
        package y2023

        import utils.NotStarted
        import utils.run


        object Main2023 {

            fun runAll() {
                println("Advent Of Code 2023")
    """.trimIndent())
    appendLine()
    (1..25).forEach {
        appendLine(
            "        y$year.day$it.Main(NotStarted, NotStarted).run($it, $year)"
        )
    }
    appendLine()
    append("""
                        
            }
        }
    """.trimIndent())
}
