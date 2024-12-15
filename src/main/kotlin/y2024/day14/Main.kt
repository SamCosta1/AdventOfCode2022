package y2024.day14

import PointInt
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import puzzlerunners.NoSampleAnswer
import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.GenericGrid
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 12,
    override val part2ExpectedAnswerForSample: Any = NoSampleAnswer,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data, runMode).let { (info, robots) ->
        val t = 100
        val robotPositions = robots.map {
            (it.start + (it.velocity * t)).floorMod(PointInt(info.width, info.height))
        }

        val midX = info.width / 2
        val midY = info.height / 2

        robotPositions.count {
            it.x < midX && it.y < midY  // < <
        } * robotPositions.count {
            it.x < midX && it.y > midY  // < >
        } * robotPositions.count {
            it.x > midX && it.y > midY  // > >
        } * robotPositions.count {
            it.x > midX && it.y < midY //  > <
        }
    }

    // Answer is 7412
    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data, runMode).let { (info, robots) ->
        if (runMode == RunMode.Sample) {
            return@let NoSampleAnswer
        }

        val cache = mutableListOf<List<PointInt>>()
        var t = 0
        while (t <= info.width * info.height) {
            // Wait for 1 second

            val robotPositions = robots.map {
                (it.start + (it.velocity * t)).floorMod(PointInt(info.width, info.height))
            }

//            if (cache.contains(robotPositions)) {
//                println(t)
//                println(cache.indexOf(robotPositions))
//                break
//            }
//            cache.add(robotPositions)

            var draw = robotPositions.count { robot -> robot.adjacentPointInts().all { robotPositions.contains(it) } } > 50


            if (draw) {
                println("Step: $t")
                repeat(info.height) { y ->
                    repeat(info.width) { x ->
                        print(if (robotPositions.contains(PointInt(x, y))) '#' else ' ')
                    }
                    println()
                }

                runBlocking { delay(200) }
                // Clear the console
                print("\u001B[2J") // Clear entire screen
                print("\u001B[H") // Move cursor to top-left corner
            }



            t++
        }
    }
}

private fun countRanges(edgePoints: MutableList<Int>): Int {
    if (edgePoints.size == 0) {
        return 0
    }

    edgePoints.sort()
    var count = 1
    for (i in 0..<edgePoints.lastIndex) {
        if (edgePoints[i] + 1 != edgePoints[i + 1]) {
            count++
        }
    }
    return count
}