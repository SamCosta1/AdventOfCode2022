import day1.Day1Main
import day10.Day10Main
import day11.Day11Main
import day12.Day12Main
import day13.Day13Main
import day14.Day14Main
import day15.Day15Main
import day16.Day16Main
import day17.Day17Main
import day18.Day18Main
import day2.Day2Main
import day20.Day20Main
import day21.Day21Main
import day22.Day22Main
import day3.Day3Main
import day23.Day23Main
import day24.Day24Main
import day25.Day25Main
import day4.Day4Main
import day5.Day5Main
import day6.Day6Main
import day7.Day7Main
import day8.Day8Main
import day9.Day9Main
import kotlin.system.measureTimeMillis

fun main() {
    println("Day 25 P1 Sample ${run { Day25Main("sample.txt").run() }}")
    println("Day 25 P1 Real   ${run { Day25Main("data.txt").run() }}")
//    println("Day 25 P2 Sample ${run { Day25Main("sample.txt").runPart2() }}")
//    println("Day 25 P2 Real   ${run { Day25Main("data.txt").runPart2() }}")
    println()
}

private fun run(repeats: Int = 1, block: () -> Any): String {
    var answer: Any? = null
    val time = measureTimeMillis {
        repeat(repeats) {
            answer = block()
        }
    }

    return "$answer | Average time ${time / repeats}ms"
}

private fun done() {
    println("Day 01 ${Day1Main.run()}")
    println()

    println("Day 02 P1 ${Day2Main.runPart1()}")
    println("Day 02 P2 ${Day2Main.runPart2()}")
    println()

    println("Day 03 ${Day3Main.run()}")
    println("Day 03 P2 ${Day3Main.runPart2()}")
    println()

    println("Day 04 P1 ${Day4Main.run()}")
    println("Day 04 P2 ${Day4Main.runPart2()}")
    println()

    println("Day 05 P1 ${Day5Main().run()}")
    println("Day 05 P2 ${Day5Main().runPart2()}")
    println()

    println("Day 06 P1 ${Day6Main.run()}")
    println("Day 06 P2 ${Day6Main.runPart2()}")
    println()

    println("Day 07 P1 ${Day7Main.run()}")
    println("Day 07 P2 ${Day7Main.runPart2()}")
    println()

    println("Day 08 P1 ${Day8Main.run()}")
    println("Day 08 P2 ${Day8Main.runPart2()}")
    println()

    println("Day 09 P1 ${Day9Main.run()}")
    println("Day 09 P2 ${Day9Main.runPart2()}")
    println()

    println("Day 10 P1 ${Day10Main.run()}")
    println("Day 10 P2 \n${Day10Main.runPart2()}")
    println()

    println("Day 11 P1 ${Day11Main().run()}")
    println("Day 11 P2 ${Day11Main().runPart2()}")
    println()

    println("Day 12 P1 ${Day12Main().run()}")
//    println("Day 12 P2 ${Day12Main().runPart2()}") // Slow as hell, uncomment if you want it
    println()

    println("Day 13 P1 ${Day13Main().run()}")
    println("Day 13 P2 ${Day13Main().runPart2()}")
    println()

    println("Day 14 P1 ${Day14Main().run()}")
    println("Day 14 P2 ${Day14Main().runPart2()}")
    println()

    println("Day 15 P1 Sample ${Day15Main("sample.txt").run(y = 10L)}")
    println("Day 15 P1 Real   ${Day15Main("data.txt").run(y = 2000000L)}")
    println("Day 15 P2 Sample ${Day15Main("sample.txt").runPart2(0L, 20L)}")
    println("Day 15 P2 Real   ${Day15Main("data.txt").runPart2(0L, 4000000L)}")
    println()

    println("Day 15 P1 Sample ${run { Day15Main("sample.txt").run(y = 10L) }}")
    println("Day 15 P1 Real   ${run { Day15Main("data.txt").run(y = 2000000L) }}")
    println("Day 15 P2 Sample ${run { Day15Main("sample.txt").runPart2(0L, 20L) }}")
    println("Day 15 P2 Real   ${run { Day15Main("data.txt").runPart2(0L, 4000000L) }}")
    println()

    println("Day 16 P1 ${run { Day16Main("sample.txt", 30).run() }}")
    println("Day 16 P1 ${run { Day16Main("data.txt", 30).run() }}")
    println("Day 16 P2 ${run { Day16Main("sample.txt", 26).runPart2() }}")
    println("Day 16 P2 ${run { Day16Main("data.txt", 26).runPart2() }}")
    println()

    println("Day 17 P1 Sample ${run { Day17Main("sample.txt").run(2022) }}")
    println("Day 17 P1 Real   ${run { Day17Main("data.txt").run(2022) }}")
    println("Day 17 P2 Sample ${run { Day17Main("sample.txt").run(1000000000000L) }}")
    println("Day 17 P2 Real   ${run { Day17Main("data.txt").run(1000000000000L) }}")
    println()

    println("Day 18 P1 Sample ${run { Day18Main("sample.txt").run() }}")
    println("Day 18 P1 Real   ${run { Day18Main("data.txt").run() }}")
    println("Day 18 P2 Sample ${run { Day18Main("sample.txt").runPart2() }}")
    println("Day 18 P2 Real   ${run { Day18Main("data.txt").runPart2() }}")
    println()

    //    println("Day 19 P1 Sample ${run { Day19Main(24, "sample.txt").run() }}")
//    println("Day 19 P1 Real   ${run { Day19Main(24, "data.txt").run() }}")
//    println("Day 19 P2 Real   ${run { Day19Main(32, "data.txt").runPart2() }}")
//    println("Day 19 P2 Sample ${run { Day19Main(32, "sample.txt").runPart2() }}")
//    println()

    println("Day 20 P1 Sample ${run { Day20Main("sample.txt").run() }}")
    println("Day 20 P1 Real   ${run { Day20Main("data.txt").run() }}")
    println("Day 20 P2 Sample ${run { Day20Main("sample.txt").runPart2() }}")
    println("Day 20 P2 Real   ${run { Day20Main("data.txt").runPart2() }}")
    println()

    println("Day 21 P1 Sample ${run { Day21Main("sample.txt").run() }}")
    println("Day 21 P1 Real   ${run { Day21Main("data.txt").run() }}")
    println("Day 21 P2 Sample ${run { Day21Main("sample.txt").runPart2() }}")
    println("Day 21 P2 Real   ${run { Day21Main("data.txt").runPart2() }}")
    println()

    println("Day 22 P1 Sample ${run { Day22Main("sample.txt").run() }}")
    println("Day 22 P1 Real   ${run { Day22Main("data.txt").run() }}")
    println("Day 22 P2 Sample ${run { Day22Main("sample.txt").runPart2(4) }}")
    println("Day 22 P2 Real   ${run { Day22Main("data.txt").runPart2(50) }}")
    println()

    println("Day 23 P1 Sample ${run { Day23Main("sample.txt").run() }}")
    println("Day 23 P1 Real   ${run { Day23Main("data.txt").run() }}")
    println("Day 23 P2 Sample ${run { Day23Main("sample.txt").runPart2() }}")
    println("Day 23 P2 Real   ${run { Day23Main("data.txt").runPart2() }}")
    println()

    println("Day 24 P1 Sample ${run { Day24Main("sample.txt").run() }}")
    println("Day 24 P1 Real   ${run { Day24Main("data.txt").run() }}")
    println("Day 24 P2 Sample ${run { Day24Main("sample.txt").runPart2() }}")
    println("Day 24 P2 Real   ${run { Day24Main("data.txt").runPart2() }}")
    println()
}
