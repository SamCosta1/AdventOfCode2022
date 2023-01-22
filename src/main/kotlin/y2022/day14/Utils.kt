package y2022.day14

import y2022.day14.Day14Main.*
import kotlin.math.max
import kotlin.math.min

// Fills in a line, for example given [(0,0), (2,0)] then returns [(0,0), (1,0), (2,0)]
fun List<Point>.fillInGaps() = mutableListOf<Point>().also { results ->
    for (i in 0 until size - 1) {
        val p1 = get(i)
        val p2 = get(i + 1)

        // Not supporting diagonal motion
        if (p1.x == p2.x) {
            minMaxRange(p1.y,p2.y).forEach { results.add(Point(p1.x, it)) }
        }

        if (p1.y == p2.y) {
            minMaxRange(p1.x, p2.x).forEach { results.add(Point(it, p1.y)) }
        }
    }
}

private fun minMaxRange(int1: Int, int2: Int) = min(int1,int2)..max(int1,int2)