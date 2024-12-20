package y2024.day19

import utils.split

object Parser {
    fun parse(data: List<String>) = data.split { it.isBlank() }.let { (towels, combos) ->
        towels.first().split(", ").toSet() to combos
    }

}