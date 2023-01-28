package y2021.day10

import utils.Puzzle
import utils.RunMode

class Main : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.sumOf { line ->
        val res = runCatching { createChunks(0, line) }
        val e = res.exceptionOrNull() as? CorruptException ?:  res.exceptionOrNull()?.let { throw it }

        e?.corrupt?.score ?: 0L
    }

    data class Chunk(val startChar: Char, val chunks: List<Chunk> = emptyList(), val isIncomplete: Boolean = false)

    class CorruptException(val corrupt: Char) : java.lang.Exception()

    fun createChunks(startIndex: Int, line: String): Pair<Int, Chunk>? {
        val subChunks = mutableListOf<Chunk>()

        if (!openChars.contains(line[startIndex])) {
            throw java.lang.Exception("Invalid 1")
        }

        var index: Int = startIndex + 1
        while (index < line.length) {

            if (line[startIndex].closing == line[index]) {
                return Pair(index + 1, Chunk(line[startIndex], subChunks))
            } else if (closeChar.contains(line[index])) {
                throw CorruptException(line[index])
            }

            if (openChars.contains(line[index])) {
                val result = createChunks(index, line)!!
                subChunks.add(result.second)
                index = result.first
            } else {
                val res = createChunks(index, line)
                return res?.let { Pair(res.first, res.second) }
            }
//            index++
        }

        return Pair(index, Chunk(line[startIndex], emptyList(), true))
    }

    val openChars = listOf(
        '[',
        '(',
        '{',
        '<'
    )


    val closeChar = listOf(
        ']',
        ')',
        '}',
        '>'
    )
    val Char.closing
        get() = when (this) {
            '[' -> ']'
            '(' -> ')'
            '{' -> '}'
            '<' -> '>'
            else -> throw Exception()
        }

    val Char.score
        get() = when (this) {
            ']' -> 57
            ')' -> 3
            '}' -> 1197
            '>' -> 25137
            null -> 0
            else -> throw Exception()
        }.toLong()

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
