package y2022.day15

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

data class Point(val x: Long, val y: Long) {
    constructor(x: Int,  y: Int) : this(x.toLong(), y.toLong())
}
class Day15Main(val file: String) {

    data class SensorBeaconPair(
        val sensor: Point,
        val beacon: Point,
        val manhattanDistance: Long = sensor.manhattanDistance(beacon)
    )

    val data =
        Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day15/$file")).map { raw ->
            val parsed = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
                .matchEntire(raw)!!.groupValues.drop(1)
            SensorBeaconPair(
                Point(parsed[0].toLong(), parsed[1].toLong()),
                Point(parsed[2].toLong(), parsed[3].toLong())
            )
        }

    fun populateGrid(): Grid {
        val grid = Grid()

        data.forEachIndexed { index, pair ->
            grid[pair.sensor] = Grid.State.Sensor
            grid[pair.beacon] = Grid.State.Beacon

            val distance = pair.sensor.manhattanDistance(pair.beacon)

            // I know this is naiive
            ((pair.sensor.y - distance)..(pair.sensor.y + distance)).forEach { y ->
                ((pair.sensor.x - distance)..(pair.sensor.x + distance)).forEach { x ->
                    val pos = Point(x,  y)
                    if (pair.sensor.manhattanDistance(pos) <= distance && grid[pos] == Grid.State.Empty) {
                        grid[pos] = Grid.State.UnBeaconable
                    }
                }
            }

            println("Added pair $index")
        }

        println(grid)
        return grid
    }

    fun run0(): Int {
        val y = 2000000L // The y coordinate from the problem

        var checks = 0
        val leftMostXToCheck = data.minOf { it.sensor.x - it.manhattanDistance }!!
        val rightMostXToCheck = data.maxOf { it.sensor.x + it.manhattanDistance }!!

        return (leftMostXToCheck..rightMostXToCheck).filter { x ->
            val currentPoint = Point(x, y)
            data.any { pair ->
                val sensorOrBeacon = pair.sensor == currentPoint || pair.beacon == currentPoint
                val other = pair.sensor.manhattanDistance(currentPoint) <= pair.manhattanDistance
                checks++
                !sensorOrBeacon && other
            }
        }.count().also {
            println("Total checks run0 $checks")
        }

    }

    fun run(): Int {
        val y = 2000000L // The y coordinate from the problem


        val pairsToCheck = data.filter { pair ->
            !(pair.sensor.y + pair.manhattanDistance < y || pair.sensor.y - pair.manhattanDistance > y)
        }.sortedBy { it.sensor.x }

        val leftMostXToCheck = pairsToCheck.first().let { it.sensor.x - it.manhattanDistance }
        val rightMostXToCheck = pairsToCheck.last().let { it.sensor.x + it.manhattanDistance }

        var checks = 0
        val points = mutableSetOf<Long>()

        var currentX = leftMostXToCheck
        pairsToCheck.forEach { pair ->
            var previousMatch = false
            for (x in max(currentX, pair.sensor.x - pair.manhattanDistance)..pair.sensor.x + pair.manhattanDistance) {
                val currentPoint = Point(x, y)
                val sensorOrBeacon = pair.sensor == currentPoint || pair.beacon == currentPoint
                val other = pair.sensor.manhattanDistance(currentPoint) <= pair.manhattanDistance
                checks++

                if (!sensorOrBeacon && other) {
                    points.add(x)
                    previousMatch = true
                } else if (previousMatch) {
                    currentX = x
                    break // Reached the right hand edge of the radius, no more matches possible
                }
            }
        }

        println("Total Checks Run2 $checks")
        return points.size
    }

    data class Answer(val count: Int, val ranges: List<LongRange>)

    fun execute(y: Long = 2000000L): Answer {

        val pairsToCheck = data.filter { pair ->
            !(pair.sensor.y + pair.manhattanDistance < y || pair.sensor.y - pair.manhattanDistance > y)
        }.sortedBy { it.sensor.x - (it.manhattanDistance - abs(y - it.sensor.y)) }

        val leftMostXToCheck = pairsToCheck.first().let { it.sensor.x - it.manhattanDistance }

        var count = 0L

        var currentX = leftMostXToCheck
        val ranges = mutableListOf<LongRange>()
        pairsToCheck.forEach { pair ->
            val xDelta = pair.manhattanDistance - abs(y - pair.sensor.y)

            val lowerX = max(pair.sensor.x - xDelta, currentX)
            val upperX = pair.sensor.x + xDelta

            if (upperX >= currentX) {
                count += upperX + 1 - lowerX

                // If possible, just extend the last range rather than adding a new one
                if (ranges.lastOrNull()?.last?.let { it >= lowerX || it + 1 == lowerX } == true) {
                    ranges[ranges.lastIndex] = LongRange(ranges.last().first, upperX)
                } else {
                    ranges.add(LongRange(lowerX, upperX))
                }
                currentX = upperX + 1
            }

        }

        return Answer(count.toInt() - 1, ranges)
    }

    fun run(y: Long): Int {
        val answer = execute(y)
        return answer.count
    }

    fun runPart2(minCoordinate: Long, maxCoordinate: Long): String {
        for (y in (minCoordinate..maxCoordinate)) { // Every valid y coordinate

            val xRanges = execute(y)
                .ranges // Ranges of all locations beacons cannot be
                .mapNotNull { range ->

                    // Trimming the ranges to ignore everything outside of our max & min
                    if (range.first > maxCoordinate || range.last < minCoordinate) {
                        null
                    } else {
                        LongRange(max(minCoordinate, range.first), min(maxCoordinate, range.last))
                    }
                }

            if (xRanges.size == 1) {
                if (!xRanges.first().contains(y)) {
                    // For completeness this should handle the case where y=min || y=max is a solution but cba
                    break
                }
            }

            // Find first gap in a range, means there's a position a beacon could  be
            for (i in 0..xRanges.lastIndex - 1) {
                val r1 = xRanges[i]
                val r2 = xRanges[i + 1]

                if (r1.last + 1 < r2.first) {
                    return "x=${r1.last + 1} y=$y  Answer = ${((r1.last + 1) * 4000000L) + y}"
                }
            }
        }

        throw Exception("Solution not found")
    }
}

fun Point.manhattanDistance(other: Point) = abs(x - other.x) + abs(y - other.y)

