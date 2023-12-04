package y2023.day4

import utils.Puzzle
import utils.RunMode
import java.util.LinkedList
import kotlin.math.pow

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).sumOf { card ->
        if (card.numMatches == 0) {
            0L
        } else {
            2.0.pow(card.numMatches - 1.0).toLong()
        }
    }
    override fun runPart2(data: List<String>, runMode: RunMode): Any {
        val cards = LinkedList(Parser.parse(data))

        var index = 0
        while (index < cards.size) {
            val card = cards[index]
            repeat(card.numMatches) {
                cards.add(cards[card.cardNumber + it])
            }
            index++
        }
        return cards.size.toLong()
    }
}
