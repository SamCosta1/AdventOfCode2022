@file:Suppress("UNCHECKED_CAST")

package puzzlerunners

import puzzlerunners.output.AsciiTableGenerator
import utils.runTimedNew
import y2023.Main2023
import kotlin.system.measureTimeMillis

interface YearOfPuzzles {
    val puzzles: List<Puzzle>
    val year: Int

    fun runAll() {
        val yearResult = runTimedNew {
            Main2023.puzzles.mapIndexed { index, puzzle ->
                puzzle.run(index + 1, year)
            }
        }.let {
            YearResults(it.solution as List<DayResults>, it.runtime, year)
        }

        println(AsciiTableGenerator.format(yearResult))
    }

    fun runLatest() = Main2023.puzzles.indexOfLast { it.part1ExpectedAnswerForSample != NotStarted }.let {
        Main2023.puzzles[it].run(it + 1, year)
    }

    fun run(day: Int) {
        Main2023.puzzles[day - 1].run(day, year)
    }
}