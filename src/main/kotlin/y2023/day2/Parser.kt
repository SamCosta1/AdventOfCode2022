package y2023.day2

object Parser {


    data class Game(val id: Int, val sets: List<List<Entry>>) {
        data class Entry(val color: String, val count: Int)
    }

    fun parse(data: List<String>) = data.mapIndexed { index, row ->
        val sets = row.split(":").last().split(";").map {
            it.trim().split(", ").map { entryRaw ->
                val entry = entryRaw.split(" ")
                Game.Entry(entry[1], entry[0].toInt())
            }
        }
        Game(index + 1, sets)
    }
}