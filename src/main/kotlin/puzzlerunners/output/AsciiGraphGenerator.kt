package puzzlerunners.output

import com.mitchtalmadge.asciidata.graph.ASCIIGraph
import puzzlerunners.YearResults

object AsciiGraphGenerator {
    fun formatGraph(yearResults: YearResults): String = buildString {
        "Part1 real + Part2 real runtimes by day".let {
            appendLine((0..it.length).joinToString("") { "=" })
            appendLine(it)
            appendLine((0..it.length).joinToString("") { "=" })
        }
        appendLine()
        append(ASCIIGraph
            .fromSeries(yearResults.toDoubleArray())
            .withNumRows(10)
            .plot())
    }

    private fun YearResults.toDoubleArray(): DoubleArray = days.dropLastWhile {
        !it.isComplete
    }.map { (it.part2Real?.runtime ?: 0L) + (it.part1Real?.runtime ?: 0L)}
        .map { it / 1_000_000.0 }
        .flatMap { listOf(it, it, it) }
        .toDoubleArray()
}