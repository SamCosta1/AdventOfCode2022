package y2024.day4

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.RunMode
import kotlin.math.abs

class Main(
    override val part1ExpectedAnswerForSample: Any = 18,
    override val part2ExpectedAnswerForSample: Any = 9,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Int {
        val rows = data.sumOf { row -> row.dropLast(3).asSequence().countXmases { row[it] } }
        val cols = (0..<data.first().length).sumOf { col ->
            val colsAccessor = { rowIndex: Int ->
                data[rowIndex][col]
            }
            sequence {
                data.dropLast(3).forEachIndexed { index, s ->
                    yield(colsAccessor(index))
                }
            }.countXmases(colsAccessor)
        }

        val leftDiags = (0..<data.size * 2).sumOf { k ->
            val str = buildString {
                (0..k).forEach { col ->
                    val row = k - col
                    if (row < data.size && col < data.size) {
                        append(data[row][col])
                    }
                }
            }

            str.dropLast(3).asSequence().countXmases { str[it] } ?: 0
        }
        val rightDiags = (0..<data.size * 2).sumOf { k ->
            val str = buildString {
                (0..k).forEach { col ->
                    val row = k - col
                    if (row < data.size && col < data.size) {
                        append(data[row][data.size - 1 - col])
                    }
                }
            }

            str.dropLast(3).asSequence().countXmases { str[it] } ?: 0
        }

        return rows + cols + leftDiags + rightDiags
    }

    private val expectDiag = setOf('M', 'S')
    override fun runPart2(data: List<String>, runMode: RunMode) = data.dropLast(2).mapIndexed { rowIndex, row ->
        row.dropLast(2).mapIndexed { colIndex, c ->
            val diag1 = setOf(c, data[rowIndex + 2][colIndex + 2])
            val diag2 = setOf(data[rowIndex + 2][colIndex], data[rowIndex][colIndex + 2])
            data[rowIndex+1][colIndex+1] == 'A' && diag1 == diag2 && diag1 == expectDiag
        }.count { it }
    }.sum()

    fun Sequence<Char>.countXmases(atIndex: (Int) -> Char) = mapIndexed { index, c ->
            val thisBit = "$c${atIndex(index + 1)}${atIndex(index + 2)}${atIndex(index + 3)}"
            (thisBit == "XMAS" || thisBit == "XMAS".reversed())

    }.count { it }
}
