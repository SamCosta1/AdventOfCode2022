package y2023.day24

import utils.Point3D
import utils.getAlphabetLetter
import kotlin.math.abs
import kotlin.math.sqrt

object Parser {
    data class HailStone(val label: String, val pos: Point3D, val velocity: Point3D) {
        override fun toString(): String {
            return "${pos.x}, ${pos.y}, ${pos.z}, @ ${velocity.x}, ${velocity.y}, ${velocity.z}"
        }

        fun at(t1: Long) = Point3D(
            pos.x + velocity.x * t1,
            pos.y + velocity.y * t1,
            pos.z + velocity.z * t1,
        )

        fun distanceToLine(a: Point3D, b: Point3D) = sqrt(
            abs(a.x - b.x) * abs(a.x - b.x) +
                    abs(a.y - b.y) * abs(a.y - b.y) +
                    abs(a.z - b.z) * abs(a.z - b.z) .toDouble()
        )
    }
    fun parse(data: List<String>) = data.mapIndexed { index, row ->
        val split = row.split(" @ ").map { section ->
            section.split(", ").map { it.trim().toLong() }
        }
        val (p, v) = split
        val (px, py, pz) = p
        val (vx, vy, vz) = v
        HailStone(getAlphabetLetter(index), Point3D(px, py, pz), Point3D(vx, vy, vz))
    }
}
