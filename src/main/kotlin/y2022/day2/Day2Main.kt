package y2022.day2

import puzzlerunners.Puzzle
import utils.RunMode
import java.nio.file.Files
import java.nio.file.Paths

class Main(
    override val part1ExpectedAnswerForSample: Any = 15,
    override val part2ExpectedAnswerForSample: Any = 12,
    override val isComplete: Boolean = true
) : Puzzle {

    enum class Move(val score: Int) {
        Rock(1),
        Paper(2),
        Scissors(3);

        fun winningScore(otherMove: Move) = when (this) {
            Rock -> when (otherMove) {
                Rock -> 3
                Paper -> 0
                Scissors -> 6
            }

            Paper -> when (otherMove) {
                Rock -> 6
                Paper -> 3
                Scissors -> 0
            }

            Scissors -> when (otherMove) {
                Rock -> 0
                Paper -> 6
                Scissors -> 3
            }
        }
    }

    private fun myScore(myPlay: Move, theirPlay: Move) = myPlay.score + myPlay.winningScore(theirPlay)

    override fun runPart1(data: List<String>, runMode: RunMode): Any = data.map { tuple ->
        tuple.split(" ").map {
            when (it) {
                "A", "X" -> Move.Rock
                "B", "Y" -> Move.Paper
                "C", "Z" -> Move.Scissors
                else -> throw Exception("Broke innit")
            }
        }
    }.sumBy { myScore(it.last(), it.first()) }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = data.map { tuple ->
        val split = tuple.split(" ")
        val theirPlay = when (split.first()) {
            "A" -> Move.Rock
            "B" -> Move.Paper
            "C" -> Move.Scissors
            else -> throw Exception("Broke innit")
        }

        val myPlay = when (split.last()) {
            "X" -> Move.values()[Math.floorMod(Move.values().indexOf(theirPlay) - 1, 3)] // Lose
            "Y" -> theirPlay // Draw
            "Z" -> Move.values()[Math.floorMod(Move.values().indexOf(theirPlay) + 1, 3)] // Win
            else -> throw Exception("Broken innit")
        }

        myScore(myPlay, theirPlay)
    }.sum()
}