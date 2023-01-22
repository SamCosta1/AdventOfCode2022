package y2021

import utils.*
import utils.runTimed

object Main2021 {
    private val allDays =
        (1..25).take(5).map { Class.forName("y2021.day$it.Main") }

    fun runAll() = allDays.forEachIndexed { index, puzzle ->
        p1Sample(index, puzzle)
        p1Real(index, puzzle)
        p2Sample(index, puzzle)
        p2Real(index, puzzle)
        println()
    }

    private fun p2Real(index: Int, puzzle: Class<*>) {
        println(
            "Day ${(index + 1).debug()} Part 2  -  Real: ${
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
    }

    private fun p2Sample(index: Int, puzzle: Class<*>) {
        println(
            "Day ${(index + 1).debug()} Part 2 - Sample: ${
                runTimed {
                    (puzzle.getConstructor().newInstance() as Puzzle).runPart2(
                        parseFile(
                            2021,
                            index + 1,
                            "sample.txt"
                        ), RunMode.Sample
                    )
                }
            }"
        )
    }

    private fun p1Real(index: Int, puzzle: Class<*>) {
        println(
            "Day ${(index + 1).debug()} Part 1  -  Real: ${
                runTimed {
                    (puzzle.getConstructor().newInstance() as Puzzle).runPart1(
                        parseFile(
                            2021,
                            index + 1,
                            "data.txt"
                        ), RunMode.Real
                    )
                }
            }"
        )
    }

    private fun p1Sample(index: Int, puzzle: Class<*>) {
        println(
            "Day ${(index + 1).debug()} Part 1 - Sample: ${
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
    }
}