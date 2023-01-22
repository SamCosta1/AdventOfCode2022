package y2022.day14

import java.nio.file.Files
import java.nio.file.Paths

typealias DidComeToRest = Boolean

class Day14Main {

    data class Point(val x: Int, val y: Int)

    val sandOrigin = Point(500, 0)

    val data =
        Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day14/data.txt")).map { raw ->
            raw.split(" -> ").map {
                val strPair = it.split(",")
                Point(strPair.first().toInt(), strPair.last().toInt())
            }
        }.let { linesOfRock ->
            val grid = Grid()
            linesOfRock.forEach {
                grid.addAll(it.fillInGaps(), Grid.State.Rock)
            }

            grid
        }

    fun emitSandGrain(): DidComeToRest {
        var sandPosition = sandOrigin.copy(y = sandOrigin.y)

        while (true) {
            if (data.isBelowBottomPoint(sandPosition) || data.isBlocked(sandOrigin)) {
                return false // Gone into the abyss
            }

            val nextPosition = sandPosition.computeNextPosition()

            // Come to rest
            if (nextPosition == sandPosition) {
                data[nextPosition] = Grid.State.Sand
                return true
            }

            // Keep going
            sandPosition = nextPosition
        }
    }

    private fun Point.computeNextPosition(): Point = listOf(
        copy(y = y + 1),          // Below
        copy(x = x - 1, y = y + 1),   // Diagonal Left
        copy(x = x + 1, y = y + 1)      // Diagonal Right
    ).firstOrNull { !data.isBlocked(it) } ?: this // If nowhere to go, it stays put

    fun run(): String {
        var unitsEmitted = 0

        while (true) {
            if (emitSandGrain()) {
                unitsEmitted++
            } else {
                break
            }
        }
        return unitsEmitted.toString()
    }

    fun runPart2(): String {
        data.hasFloor = true
        return run()
    }
}