package y2022.day9

import java.nio.file.Files
import java.nio.file.Paths
import kotlin.math.abs
import kotlin.math.sign

object Main {

    data class Move(val direction: Direction, val moves: Int)
    data class Position(val x: Int, val y: Int)
    enum class Direction {
        Up,
        Down,
        Left,
        Right
    }

    val data =
        Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day9/data.txt")).map { raw ->
            val split = raw.split(" ")
            Move(
                when (split.first()) {
                    "U" -> Direction.Up
                    "D" -> Direction.Down
                    "R" -> Direction.Right
                    "L" -> Direction.Left
                    else -> throw Exception("invalid direction")
                }, split.last().toInt()
            )
        }

    fun run(): String {

        var headPos = Position(0, 0) // Bottom left
        var tailPos = Position(0, 0) // Bottom left

        val tailPositions = mutableSetOf(tailPos)
        val headPositions = mutableSetOf(headPos)

        data.forEach { move ->
            repeat(move.moves) {
                headPos = computeNewPosition(headPos, move.direction)

                tailPos = computeNewTailPos(tailPos, headPos)

                headPositions.add(headPos)
                tailPositions.add(tailPos)
            }
        }

        return tailPositions.size.toString()
    }

    private fun Position.isTouching(
        otherPos: Position
    ) = abs(x - otherPos.x) <= 1 && abs(y - otherPos.y) <= 1

    private fun computeNewPosition(oldPos: Position, direction: Direction) = when (direction) {
        Direction.Up -> oldPos.copy(y = oldPos.y + 1)
        Direction.Down -> oldPos.copy(y = oldPos.y - 1)
        Direction.Left -> oldPos.copy(x = oldPos.x - 1)
        Direction.Right -> oldPos.copy(x = oldPos.x + 1)
    }

    fun runPart2(): String {
        val positions = Array(10) { Position(0, 0) }

        val tailPositions = mutableSetOf(positions.last())

        data.forEach { move ->
            repeat(move.moves) {
                positions[0] = computeNewPosition(positions[0], move.direction)

                (1 until positions.size).forEach { index ->
                    positions[index] = computeNewTailPos(positions[index], positions[index - 1])
                }

                tailPositions.add(positions.last())
            }
        }

        return tailPositions.size.toString()
    }

    private fun computeNewTailPos(tailPos: Position, newHeadPos: Position): Position {
        if (tailPos.isTouching(newHeadPos)) {
            return tailPos
        }

        return if (abs(tailPos.x - newHeadPos.x) == 2 && tailPos.y == newHeadPos.y) {
            newHeadPos.copy(x = tailPos.x + (newHeadPos.x - tailPos.x).sign)
        } else if (abs(tailPos.y - newHeadPos.y) == 2 && tailPos.x == newHeadPos.x) {
             newHeadPos.copy(y = tailPos.y + (newHeadPos.y - tailPos.y).sign)
        } else {
            newHeadPos.copy(x = tailPos.x + (newHeadPos.x - tailPos.x).sign, y = tailPos.y + (newHeadPos.y - tailPos.y).sign)
        }
    }
}