package y2021.day10

import utils.Puzzle
import utils.RunMode
import java.lang.StringBuilder

class Main(override val part1ExpectedAnswerForSample: Any, override val part2ExpectedAnswerForSample: Any) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.sumOf { line ->
        val res = runCatching { createChunks(line) }
        val e = res.exceptionOrNull() as? CorruptException ?: res.exceptionOrNull()?.let { throw it }

        e?.corrupt?.score ?: 0L
    }

    data class Chunk(val startChar: Char, val chunks: List<Chunk> = emptyList(), val isIncomplete: Boolean = false) {

    }

    class CorruptException(val corrupt: Char) : java.lang.Exception()

    fun createChunks(line: String): List<Chunk> {
        var index = 0
        val chunks = mutableListOf<Chunk>()
        while (index < line.length) {
            val res = createChunks(index, line)
            index = res.first
            chunks += res.second
        }

        return chunks
    }

    fun createChunks(startIndex: Int, line: String): Pair<Int, Chunk> {
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
            }
        }

        return Pair(index, Chunk(line[startIndex], subChunks, true))
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

    val Char.p2Score
        get() = when (this) {
            ']' -> 2
            ')' -> 1
            '}' -> 3
            '>' -> 4
            null -> 0
            else -> throw Exception()
        }.toLong()

    override fun runPart2(data: List<String>, runMode: RunMode) = data.mapNotNull { line ->
        val res = runCatching { createChunks(line) }.getOrNull()
        if (res != null) {

            val closing = StringBuilder().also { getChars(res, it) }

            var score = 0L
            closing.toString().forEach { char ->
                score = score * 5L + char.p2Score
            }

            score
        } else {
            null
        }
    }.sorted().let { it[it.size / 2] }

    fun getChars(chunks: List<Chunk>, stringBuilder: StringBuilder) {
        chunks.forEach { chunk ->
            chunk.chunks.forEach { getChars(listOf(it), stringBuilder) }
            if (chunk.isIncomplete) {
                stringBuilder.append(chunk.startChar.closing)
            }
        }
    }

    fun printClosing(chunks: List<Chunk>) {
        chunks.forEach { chunk ->
            chunk.chunks.forEach { printClosing(listOf(it)) }
            if (chunk.isIncomplete) {
                print(chunk.startChar.closing)
            }
        }
    }
}
/*
[(()[<>])]({[<{<<[]>>(

    Chunk(startChar=[,
        chunks=[Chunk(startChar=(, chunks=[
            Chunk(startChar=(, chunks=[], isIncomplete=false), // closed
            Chunk(startChar=[, chunks=[ // closed
                Chunk(startChar=<, chunks=[], isIncomplete=false)], isIncomplete=false)], isIncomplete=false)], isIncomplete=false)


*/
