package y2023.day7

object Parser {
    data class Hand(val bidAmount: Long, val cards: List<Int>, val raw: String) {
        enum class Type {
            FiveOAK,
            FourOAK,
            FullHouse,
            ThreeOfAKind,
            TwoPair,
            OnePair,
            HighCard
        }
        val type = cards.type()
        val sortedCards = cards.sorted()
        val pretty = "$raw $bidAmount"
    }

    fun parse(data: List<String>) = data.map { row ->
        val split = row.split(" ")
        Hand(
            bidAmount = split.last().toLong(),
            cards =   split.first().map {
                when(it) {
                    'T' -> 10
                    'J' -> 11
                    'Q' -> 12
                    'K' -> 13
                    'A' -> 14
                    else -> it.toString().toInt()
                }
            },
            raw = split.first()
        )
    }

    fun List<Int>.type(): Hand.Type {
        val groups = groupingBy { it }.eachCount()

        return when {
            groups.size == 1 -> Hand.Type.FiveOAK
            groups.any { it.value == 4 } -> Hand.Type.FourOAK
            groups.any { it.value == 3 } && groups.size == 2 -> Hand.Type.FullHouse
            groups.any { it.value == 3 } -> Hand.Type.ThreeOfAKind
            groups.count { it.value == 2 } == 2 -> Hand.Type.TwoPair
            groups.count { it.value == 2 } == 1 -> Hand.Type.OnePair
            groups.size == 5 -> Hand.Type.HighCard
            else -> throw Exception("Something broke $this")
        }
    }
}