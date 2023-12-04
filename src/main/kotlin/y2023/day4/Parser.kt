package y2023.day4

object Parser {

    data class Card(val cardNumber: Int, val id: String, val winning: List<Long>, val cardNumbers: Set<Long>) {
        val numMatches = cardNumbers.intersect(winning.toSet()).size
    }

    fun parse(data: List<String>) = data.map { row ->
        val firstSplit = row.split(":")
        val id = firstSplit.first().trim()

        val (winning, cards) = firstSplit.last().split("|").map { it.trim() }
        Card(
            id = id,
            cardNumber = id.split(" ").last().toInt(),
            winning = winning.split(" ").mapNotNull { it.toLongOrNull() },
            cardNumbers = cards.split(" ").mapNotNull { it.toLongOrNull() }.toSet()
        )
    }
}