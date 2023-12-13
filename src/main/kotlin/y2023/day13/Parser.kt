package y2023.day13

import puzzlerunners.Puzzle
import y2023.day13.Parser.rotate90

object Parser {


    data class Pattern(val grid: List<List<Char>>) {
        val rotated90Degres by lazy { Pattern(grid.rotate90()) }

        override fun toString(): String = buildString {
            grid.forEach {
                it.forEach(::append)
                appendLine()
            }
            appendLine()
        }
    }

    fun parse(data: List<String>): List<Pattern> {
        return parseRaw(data)
    }

    fun <T> List<List<T>>.rotate90(): MutableList<List<T>> {
        val newList = mutableListOf<List<T>>()

        repeat(first().size) { i ->
            val list = mutableListOf<T>()
            repeat(size) { j ->
                list.add(this[size - j - 1][i])
            }
            newList.add(list)
        }
        return newList
    }

    private fun parseRaw(data: List<String>): MutableList<Pattern> {
        val result = mutableListOf<Pattern>()
        var thisPattern = mutableListOf<List<Char>>()

        data.forEach {
            if (it.isBlank()) {
                result.add(Pattern(thisPattern))
                thisPattern = mutableListOf()
            } else {
                thisPattern.add(it.toList())
            }
        }
        if (thisPattern.isNotEmpty()) {
            result.add(Pattern(thisPattern))
        }
        return result
    }

}