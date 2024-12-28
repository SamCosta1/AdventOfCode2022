package y2024.day25

import utils.split

object Parser {
    data class Info(val keys: List<Array<Int>>, val locks: List<Array<Int>>)
    fun parse(data: List<String>): Info {
        val keys = mutableListOf<Array<Int>>()
        val locks = mutableListOf<Array<Int>>()
        data.split { it.isBlank() }.forEach { block ->
            val heights = Array(block.first().length) { -1 }
            for (x in block.first().indices) {
                for (y in block.indices) {
                    heights[x] += if (block[y][x] == '#') 1 else 0
                }
            }

            if (block.first().contains("#")) {
                locks.add(heights)
            } else {
                keys.add(heights)
            }
        }

        return Info(locks, keys)
    }
}