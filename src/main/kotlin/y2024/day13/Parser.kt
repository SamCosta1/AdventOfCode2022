package y2024.day13

import PointInt
import utils.Point
import utils.split

object Parser {

    data class Machine(val xa: Long, val ya: Long, val xb: Long, val yb: Long, val prize: Point)

    private val buttonRegex = ".*\\+(\\d+), Y\\+(\\d+)".toRegex()
    private val prizeRegex = ".*=(\\d+), Y=(\\d+)".toRegex()

    fun parse(data: List<String>) = data.split { it.isBlank() }.map {
        val a = buttonRegex.matchEntire(it.first())?.groupValues!!.drop(1)
        val b = buttonRegex.matchEntire(it[1])?.groupValues!!.drop(1)
        val prize = prizeRegex.matchEntire(it[2])?.groupValues!!.drop(1)
        Machine(
            xa = a[0].toLong(), ya = a[1].toLong(),
            xb = b[0].toLong(), yb = b[1].toLong(),
            prize = Point(prize[0].toLong(), prize[1].toLong()),
        )
    }
}

