package y2023.day4

import puzzlerunners.Puzzle
import utils.RunMode
import kotlin.math.pow

class Main(
    override val part1ExpectedAnswerForSample: Any = 13L,
    override val part2ExpectedAnswerForSample: Any = 30L
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).sumOf { card ->
        if (card.numMatches == 0) {
            0L
        } else {
            2.0.pow(card.numMatches - 1.0).toLong()
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { cards ->
        cards.sumOf { totalSpawnedPlusItself(cards, it) }
    }

    private val cache = mutableMapOf<Parser.Card, Long>()
    private fun totalSpawnedPlusItself(cards: List<Parser.Card>, card: Parser.Card): Long {
        return cache.getOrPut(card) {
            (1..card.numMatches).sumOf {
                totalSpawnedPlusItself(cards, cards[card.indexInList + it])
            } + 1
        }
    }
}
