package y2023.day4

import utils.Puzzle
import utils.RunMode
import java.util.LinkedList
import kotlin.math.pow

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).sumOf { card ->
        if (card.numMatches == 0) {
            0L
        } else {
            2.0.pow(card.numMatches - 1.0).toLong()
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { cards ->
        cards.sumOf { totalSpawnedPlusIt(cards, it) }
    }

    private val cache = mutableMapOf<Parser.Card, Long>()
    private fun totalSpawnedPlusIt(cards: List<Parser.Card>, card: Parser.Card): Long {
        return cache.getOrPut(card) {
            (1..card.numMatches).sumOf {
                totalSpawnedPlusIt(cards, cards[card.indexInList + it])
            } + 1
        }
    }
}
