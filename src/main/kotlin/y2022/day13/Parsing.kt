package y2022.day13

object Parsing {

    fun List<String>.parse() = this.chunked(3).map { raw ->
        Day13Main.Pair(parseItem(raw.first(), 0).first, parseItem(raw[1], 0).first)
    }

    private fun parseItem(raw: String, startIndex: Int): kotlin.Pair<Day13Main.Item.MyList, Int> {
        var index = startIndex
        if (raw[index] != '[') {
            throw Exception("Items must start with '[")
        }
        val thisList = mutableListOf<Day13Main.Item>()
        var nonListSubstring = ""

        index++
        while (index < raw.length) {
            if (raw[index] !in listOf('[', ']')) {
                nonListSubstring += raw[index]
                index++
            } else {
                thisList.addAll(nonListSubstring
                    .split(",")
                    .filter { it.isNotBlank() }
                    .map { Day13Main.Item.Integer(it.toInt()) }
                )
                nonListSubstring = ""
                when {
                    raw[index] == ']' -> return kotlin.Pair(Day13Main.Item.MyList(thisList), index + 1)
                    raw[index] == '[' -> {
                        val subList = parseItem(raw, index)
                        thisList.add(subList.first)
                        index = subList.second
                    }
                    else -> throw Exception("Unexpected character ${raw[index]}")
                }
            }
        }
        return Pair(Day13Main.Item.MyList(thisList), index)
    }
}