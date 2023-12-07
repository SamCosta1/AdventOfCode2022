package y2023.day7

import utils.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any,
    override val part2ExpectedAnswerForSample: Any
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).sortedWith(part1Comparator).calculateWinnings()

    private fun List<Parser.Hand>.calculateWinnings() = reversed().mapIndexed { index, hand ->
        (index + 1) * hand.bidAmount
    }.sum()

    private val part1Comparator = Comparator<Parser.Hand> { hand1, hand2 ->
        if (hand1.type != hand2.type) {
            return@Comparator hand1.type.compareTo(hand2.type)
        }

        hand1.cards.forEachIndexed { index, card ->
            if (card != hand2.cards[index]) {
                return@Comparator hand2.cards[index] - card
            }
        }

        0
    }
    private val part2Comparator = Comparator<Parser.Hand> { hand1, hand2 ->
        if (hand1.part2Type != hand2.part2Type) {
            return@Comparator hand1.part2Type.compareTo(hand2.part2Type)
        }

        hand1.part2Values.forEachIndexed { index, card ->
            if (card != hand2.part2Values[index]) {
                return@Comparator hand2.part2Values[index] - card
            }
        }

        0
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).sortedWith(part2Comparator).calculateWinnings()
}
