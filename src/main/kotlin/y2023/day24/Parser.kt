package y2023.day24

import utils.Point3D
import utils.getAlphabetLetter

object Parser {
    data class HailStone(val label: String, val pos: Point3D, val velocity: Point3D)
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
