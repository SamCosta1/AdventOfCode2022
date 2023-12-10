package y2021

import puzzlerunners.Puzzle
import utils.*
import utils.runTimed

object Main2021 {

    fun run(day: Int) = Class.forName("y2021.day$day.Main").also { clazz ->
        p1Sample(day, clazz)
        p1Real(day, clazz)
        p2Sample(day, clazz)
        p2Real(day, clazz)
        println()
    }

    fun runAll() = (1..25).forEach(::run)

    private fun p2Real(day: Int, puzzle: Class<*>) {
        println(
            "Day ${(day).debug()} Part 2  -  Real: ${
                runTimed {
                    (puzzle.getConstructor().newInstance() as Puzzle).runPart2(
                        parseFile(
                            2021,
                            day,
                            "data.txt"
                        ), RunMode.Real
                    )
                }
            }"
        )
    }

    private fun p2Sample(day: Int, puzzle: Class<*>) {
        println(
            "Day ${day.debug()} Part 2 - Sample: ${
                runTimed {
                    (puzzle.getConstructor().newInstance() as Puzzle).runPart2(
                        parseFile(
                            2021,
                            day,
                            "sample.txt"
                        ), RunMode.Sample
                    )
                }
            }"
        )
    }

    private fun p1Real(day: Int, puzzle: Class<*>) {
        println(
            "Day ${day.debug()} Part 1  -  Real: ${
                runTimed {
                    (puzzle.getConstructor().newInstance() as Puzzle).runPart1(
                        parseFile(
                            2021,
                            day,
                            "data.txt"
                        ), RunMode.Real
                    )
                }
            }"
        )
    }

    private fun p1Sample(day: Int, puzzle: Class<*>) {
        println(
            "Day ${(day).debug()} Part 1 - Sample: ${
                runTimed {
                    (puzzle.getConstructor().newInstance() as Puzzle).runPart1(
                        parseFile(
                            2021,
                            day,
                            "sample.txt"
                        ), RunMode.Sample
                    )
                }
            }"
        )
    }
}