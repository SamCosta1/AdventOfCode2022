package y2024.day14

import PointInt
import utils.RunMode

object Parser {

    data class Robot(val start: PointInt, val velocity: PointInt)

    data class Info(val width: Int, val height: Int)

    private val regex = "p=(-*\\d+),(-*\\d+) v=(-*\\d+),(-*\\d+)".toRegex()
    fun parse(data: List<String>, runMode: RunMode) = Pair(
        when (runMode) {
            RunMode.Sample -> Info(11, 7)
            RunMode.Real -> Info(101, 103)
        },
        data.map {
            val match = regex.matchEntire(it)!!.groupValues.drop(1)
            Robot(
                start = PointInt(match[0].toInt(), match[1].toInt()),
                velocity = PointInt(match[2].toInt(), match[3].toInt())
            )
        }
    )


}
