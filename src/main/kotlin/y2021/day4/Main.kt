package y2021.day4

import puzzlerunners.Puzzle
import utils.RunMode

class Main(override val part1ExpectedAnswerForSample: Any, override val part2ExpectedAnswerForSample: Any) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { bingo ->
        bingo.drawOrder.forEach { number ->
            bingo.boards.forEach { it.markDrawn(number) }

            val winningBoard = bingo.boards.firstOrNull { it.completedColumns + it.completedRows > 0 }

            if (winningBoard != null) {
                return@let winningBoard.grid.flatten().filter { !winningBoard.drawn.contains(it) }.sum() * number
            }
        }

        return -1
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = Parser.parse(data).let { bingo ->
        var completeBoards = setOf<String>()
        var lastBoard: Parser.Board? = null
        bingo.drawOrder.forEach { number ->
            bingo.boards.forEach { it.markDrawn(number) }

            if (completeBoards.size == bingo.boards.size - 1) {
                lastBoard = bingo.boards.first { !completeBoards.contains(it.name) }
            }

            completeBoards = bingo.boards.filter { it.completedColumns + it.completedRows > 0 }.map { it.name }.toSet()

            if (completeBoards.size == bingo.boards.size) {
                return@let lastBoard!!.grid.flatten().filter { !lastBoard!!.drawn.contains(it) }.sum() * number
            }
        }

        return -1
    }
}
