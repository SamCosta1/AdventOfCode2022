package y2023

import utils.NotStarted
import utils.run
import kotlin.system.measureTimeMillis


object Main2023 {

    val puzzles = listOf(
        y2023.day1.Main(142, 281),
        y2023.day2.Main(8, 2286),
        y2023.day3.Main(NotStarted, NotStarted),
        y2023.day4.Main(NotStarted, NotStarted),
        y2023.day5.Main(NotStarted, NotStarted),
        y2023.day6.Main(NotStarted, NotStarted),
        y2023.day7.Main(NotStarted, NotStarted),
        y2023.day8.Main(NotStarted, NotStarted),
        y2023.day9.Main(NotStarted, NotStarted),
        y2023.day10.Main(NotStarted, NotStarted),
        y2023.day11.Main(NotStarted, NotStarted),
        y2023.day12.Main(NotStarted, NotStarted),
        y2023.day13.Main(NotStarted, NotStarted),
        y2023.day14.Main(NotStarted, NotStarted),
        y2023.day15.Main(NotStarted, NotStarted),
        y2023.day16.Main(NotStarted, NotStarted),
        y2023.day17.Main(NotStarted, NotStarted),
        y2023.day18.Main(NotStarted, NotStarted),
        y2023.day19.Main(NotStarted, NotStarted),
        y2023.day20.Main(NotStarted, NotStarted),
        y2023.day21.Main(NotStarted, NotStarted),
        y2023.day22.Main(NotStarted, NotStarted),
        y2023.day23.Main(NotStarted, NotStarted),
        y2023.day24.Main(NotStarted, NotStarted),
        y2023.day25.Main(NotStarted, NotStarted),
)

    fun runAll() = measureTimeMillis { 
        puzzles.forEachIndexed { index, puzzle ->
          puzzle.run(index + 1, 2023)
        }
    }.let {
       println()
       println("Total Time: ${it}ms")
    }
    
    fun runLatest() = puzzles.indexOfLast { it.part1ExpectedAnswerForSample != NotStarted }.let {
        puzzles[it].run(it + 1, 2023)
    }
}
