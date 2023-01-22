package y2021

import utils.*
import utils.runTimed

object Main2021 {
    private val allDays =
        (1..25).map { Class.forName("y2021.day$it.Main") }

    fun runAll() = allDays.forEachIndexed { index, puzzle ->
        println(
            "Day ${(index + 1).debug()} Sample: ${
                runTimed {
                    (puzzle.getConstructor().newInstance() as Puzzle).runPart1(
                        parseFile(
                            2021,
                            index + 1,
                            "sample.txt"
                        ), RunMode.Sample
                    )
                }
            }"
        )
        println(
            "Day ${(index + 1).debug()}   Real: ${
                runTimed {
                    (puzzle.getConstructor().newInstance() as Puzzle).runPart2(
                        parseFile(
                            2021,
                            index + 1,
                            "data.txt"
                        ), RunMode.Real
                    )
                }
            }"
        )
        println()
    }
}