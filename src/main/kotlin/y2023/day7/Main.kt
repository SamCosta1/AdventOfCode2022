package y2023.day7

import utils.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).sortedWith(Comparator { hand1, hand2 ->
        if (hand1.type != hand2.type) {
            return@Comparator hand1.type.compareTo(hand2.type)
        }

        hand1.cards.forEachIndexed { index, card ->
            if (card != hand2.cards[index]) {
                return@Comparator hand2.cards[index] - card
            }
        }

        0
    }).reversed().also {
        println(it.map { it.pretty })
    }.mapIndexed { index, hand ->
        (index + 1) * hand.bidAmount
    }.sum()

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
