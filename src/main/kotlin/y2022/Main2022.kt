package y2022

import puzzlerunners.Puzzle
import puzzlerunners.YearOfPuzzles


object Main2022: YearOfPuzzles {
    override val puzzles: List<Puzzle> = listOf(
        y2022.day1.Main(),
        y2022.day2.Main(),
        y2022.day3.Main(),
        y2022.day4.Main(),
        y2022.day5.Main(),
        y2022.day6.Main(),
        y2022.day7.Main(),
    )

    override val year: Int = 2022
    //    fun runAll() {
//        println("Day 01 ${Day1Main.run()}")
//        println()
//
//        println("Day 02 P1 ${Day2Main.runPart1()}")
//        println("Day 02 P2 ${Day2Main.runPart2()}")
//        println()
//
//        println("Day 03 ${Day3Main.run()}")
//        println("Day 03 P2 ${Day3Main.runPart2()}")
//        println()
//
//        println("Day 04 P1 ${Day4Main.run()}")
//        println("Day 04 P2 ${Day4Main.runPart2()}")
//        println()
//
//        println("Day 05 P1 ${Day5Main().run()}")
//        println("Day 05 P2 ${Day5Main().runPart2()}")
//        println()
//
//        println("Day 06 P1 ${Day6Main.run()}")
//        println("Day 06 P2 ${Day6Main.runPart2()}")
//        println()
//
//        println("Day 07 P1 ${Day7Main.run()}")
//        println("Day 07 P2 ${Day7Main.runPart2()}")
//        println()
//
//        println("Day 08 P1 ${Day8Main.run()}")
//        println("Day 08 P2 ${Day8Main.runPart2()}")
//        println()
//
//        println("Day 09 P1 ${Day9Main.run()}")
//        println("Day 09 P2 ${Day9Main.runPart2()}")
//        println()
//
//        println("Day 10 P1 ${Day10Main.run()}")
//        println("Day 10 P2 \n${Day10Main.runPart2()}")
//        println()
//
//        println("Day 11 P1 ${Day11Main().run()}")
//        println("Day 11 P2 ${Day11Main().runPart2()}")
//        println()
//
//        println("Day 12 P1 ${Day12Main().run()}")
////    println("Day 12 P2 ${Day12Main().runPart2()}") // Slow as hell, uncomment if you want it
//        println()
//
//        println("Day 13 P1 ${Day13Main().run()}")
//        println("Day 13 P2 ${Day13Main().runPart2()}")
//        println()
//
//        println("Day 14 P1 ${Day14Main().run()}")
//        println("Day 14 P2 ${Day14Main().runPart2()}")
//        println()
//
//        println("Day 15 P1 Sample ${Day15Main("sample.txt").run(y = 10L)}")
//        println("Day 15 P1 Real   ${Day15Main("data.txt").run(y = 2000000L)}")
//        println("Day 15 P2 Sample ${Day15Main("sample.txt").runPart2(0L, 20L)}")
//        println("Day 15 P2 Real   ${Day15Main("data.txt").runPart2(0L, 4000000L)}")
//        println()
//
//        println("Day 15 P1 Sample ${runTimed { Day15Main("sample.txt").run(y = 10L) }}")
//        println("Day 15 P1 Real   ${runTimed { Day15Main("data.txt").run(y = 2000000L) }}")
//        println("Day 15 P2 Sample ${runTimed { Day15Main("sample.txt").runPart2(0L, 20L) }}")
//        println("Day 15 P2 Real   ${runTimed { Day15Main("data.txt").runPart2(0L, 4000000L) }}")
//        println()
//
//        println("Day 16 P1 ${runTimed { Day16Main("sample.txt", 30).run() }}")
//        println("Day 16 P1 ${runTimed { Day16Main("data.txt", 30).run() }}")
//        println("Day 16 P2 ${runTimed { Day16Main("sample.txt", 26).runPart2() }}")
//        println("Day 16 P2 ${runTimed { Day16Main("data.txt", 26).runPart2() }}")
//        println()
//
//        println("Day 17 P1 Sample ${runTimed { Day17Main("sample.txt").run(2022) }}")
//        println("Day 17 P1 Real   ${runTimed { Day17Main("data.txt").run(2022) }}")
//        println("Day 17 P2 Sample ${runTimed { Day17Main("sample.txt").run(1000000000000L) }}")
//        println("Day 17 P2 Real   ${runTimed { Day17Main("data.txt").run(1000000000000L) }}")
//        println()
//
//        println("Day 18 P1 Sample ${runTimed { Day18Main("sample.txt").run() }}")
//        println("Day 18 P1 Real   ${runTimed { Day18Main("data.txt").run() }}")
//        println("Day 18 P2 Sample ${runTimed { Day18Main("sample.txt").runPart2() }}")
//        println("Day 18 P2 Real   ${runTimed { Day18Main("data.txt").runPart2() }}")
//        println()
//
//        //    println("Day 19 P1 Sample ${runTimed {Day19Main(24, "sample.txt").run() }}")
////    println("Day 19 P1 Real   ${runTimed {Day19Main(24, "data.txt").run() }}")
////    println("Day 19 P2 Real   ${runTimed {Day19Main(32, "data.txt").runPart2() }}")
////    println("Day 19 P2 Sample ${runTimed {Day19Main(32, "sample.txt").runPart2() }}")
////    println()
//
//        println("Day 20 P1 Sample ${runTimed { Day20Main("sample.txt").run() }}")
//        println("Day 20 P1 Real   ${runTimed { Day20Main("data.txt").run() }}")
//        println("Day 20 P2 Sample ${runTimed { Day20Main("sample.txt").runPart2() }}")
//        println("Day 20 P2 Real   ${runTimed { Day20Main("data.txt").runPart2() }}")
//        println()
//
//        println("Day 21 P1 Sample ${runTimed { Day21Main("sample.txt").run() }}")
//        println("Day 21 P1 Real   ${runTimed { Day21Main("data.txt").run() }}")
//        println("Day 21 P2 Sample ${runTimed { Day21Main("sample.txt").runPart2() }}")
//        println("Day 21 P2 Real   ${runTimed { Day21Main("data.txt").runPart2() }}")
//        println()
//
//        println("Day 22 P1 Sample ${runTimed { Day22Main("sample.txt").run() }}")
//        println("Day 22 P1 Real   ${runTimed { Day22Main("data.txt").run() }}")
//        println("Day 22 P2 Sample ${runTimed { Day22Main("sample.txt").runPart2(4) }}")
//        println("Day 22 P2 Real   ${runTimed { Day22Main("data.txt").runPart2(50) }}")
//        println()
//
//        println("Day 23 P1 Sample ${runTimed { Day23Main("sample.txt").run() }}")
//        println("Day 23 P1 Real   ${runTimed { Day23Main("data.txt").run() }}")
//        println("Day 23 P2 Sample ${runTimed { Day23Main("sample.txt").runPart2() }}")
//        println("Day 23 P2 Real   ${runTimed { Day23Main("data.txt").runPart2() }}")
//        println()
//
//        println("Day 24 P1 Sample ${runTimed { Day24Main("sample.txt").run() }}")
//        println("Day 24 P1 Real   ${runTimed { Day24Main("data.txt").run() }}")
//        println("Day 24 P2 Sample ${runTimed { Day24Main("sample.txt").runPart2() }}")
//        println("Day 24 P2 Real   ${runTimed { Day24Main("data.txt").runPart2() }}")
//        println()
//
//        println("Day 25 P1 Sample ${runTimed { Day25Main("sample.txt").run() }}")
//        println("Day 25 P1 Real   ${runTimed { Day25Main("data.txt").run() }}")
//        println()
//    }
}