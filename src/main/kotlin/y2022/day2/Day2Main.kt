package y2022.day2

import java.nio.file.Files
import java.nio.file.Paths

object Day2Main {
    val data = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day2/data.txt"))

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

    fun runPart1() = data.map { tuple ->
        tuple.split(" ").map {
            when (it) {
                "A", "X" -> Move.Rock
                "B", "Y" -> Move.Paper
                "C", "Z" -> Move.Scissors
                else -> throw Exception("Broke innit")
            }
        }
    }.sumBy { myScore(it.last(), it.first()) }

    fun runPart2() = data.map { tuple ->
        val split = tuple.split(" ")
        val theirPlay = when (split.first()) {
            "A" -> Move.Rock
            "B" -> Move.Paper
            "C" -> Move.Scissors
            else -> throw Exception("Broke innit")
        }

        val myPlay = when(split.last()) {
            "X" ->  Move.values()[Math.floorMod(Move.values().indexOf(theirPlay) - 1, 3)] // Lose
            "Y" ->  theirPlay // Draw
            "Z" ->  Move.values()[Math.floorMod(Move.values().indexOf(theirPlay) + 1, 3)] // Win
            else -> throw Exception("Broken innit")
        }

        myScore(myPlay, theirPlay)
    }.sum()
}