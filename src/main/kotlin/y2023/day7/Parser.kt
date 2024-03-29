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
        val type = cards.part1Type()
        val sortedCards = cards.sorted()
        val pretty = "$raw $bidAmount"

        val part2Type = cards.part2Type()
        val part2Values = cards.map { if (it == charToValue('J')) 1 else it }
    }

    fun parse(data: List<String>) = data.map { row ->
        val split = row.split(" ")
        Hand(
            bidAmount = split.last().toLong(),
            cards =   split.first().map {
                charToValue(it)
            },
            raw = split.first()
        )
    }

    private fun charToValue(it: Char) = when (it) {
        'T' -> 10
        'J' -> 11
        'Q' -> 12
        'K' -> 13
        'A' -> 14
        else -> it.toString().toInt()
    }

    fun List<Int>.part1Type(): Hand.Type {
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

    val jValue = charToValue('J')
    fun List<Int>.part2Type(): Hand.Type {
        val groups = groupingBy { it }.eachCount()

        val mostFrequent = groups.filter {
            it.key != jValue
        }.maxByOrNull { it.value }?.key ?: return Hand.Type.FiveOAK

        val newList = map {
            if (it == jValue) {
                mostFrequent
            } else {
                it
            }
        }
        return newList.part1Type()
    }
}