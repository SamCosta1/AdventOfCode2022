@file:Suppress("UNCHECKED_CAST")

package puzzlerunners

import puzzlerunners.output.AsciiGraphGenerator
import puzzlerunners.output.AsciiTableGenerator
import utils.runTimedNew
import y2023.Main2023
import kotlin.system.measureTimeMillis

interface YearOfPuzzles {
    val puzzles: List<Puzzle>
    val year: Int

    fun runAll() = runTimedNew {
        puzzles.mapIndexed { index, puzzle ->
            puzzle.run(index + 1, year)
        }
    }.let {
        YearResults(it.solution as List<DayResults>, it.runtime, year)
    }

    fun runLatest() = puzzles.indexOfLast { it.part1ExpectedAnswerForSample != NotStarted }.let {
        puzzles[it].run(it + 1, year)
    }

    fun run(day: Int) = puzzles[day - 1].run(day, year)


    fun executeSmort() {
        if (puzzles.last { it.part1ExpectedAnswerForSample != NotStarted }.isComplete) {
            val yearResults = runAll()
            println(AsciiTableGenerator.formatInProgress(yearResults))
            println(AsciiGraphGenerator.formatGraph(yearResults))
        } else {
            println(AsciiTableGenerator.formatInProgress(runLatest()))
        }
    }
}