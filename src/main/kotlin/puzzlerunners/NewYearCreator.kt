package puzzlerunners

import java.nio.file.Files
import java.nio.file.Paths

object NewYearCreator {

    fun run(year: Int) {
        createDays(year)
        Files.write(
            Paths.get(
                System.getProperty("user.dir"),
                "src/main/kotlin/y$year/Main${year}.kt"
            ),
            yearMain(year).lines()
        )
    }

    private fun createDays(year: Int) {
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
    
    import puzzlerunners.Puzzle
    import puzzlerunners.NotStarted
    import utils.RunMode
    
    class Main(
        override val part1ExpectedAnswerForSample: Any = NotStarted,
        override val part2ExpectedAnswerForSample: Any = NotStarted,
        override val isComplete: Boolean = true
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
        }
    }
}

fun yearMain(year: Int) = buildString {
    append("""
        package y$year

        import puzzlerunners.YearOfPuzzles


        object Main$year: YearOfPuzzles {
            override val year = $year
            override val puzzles = listOf(""".trimIndent()
    )


    appendLine()
    (1..25).forEach {
        appendLine(
            "        y$year.day$it.Main(),"
        )
    }
    append("""
        )
     }
    """.trimIndent())
}
