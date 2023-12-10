package puzzlerunners.output

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import puzzlerunners.YearResults

object AsciiTableGenerator {
    fun format(yearResults: YearResults) = table {
        cellStyle {
            border = true
            paddingLeft = 1
            paddingRight = 1
        }

        header {
            row {
                cell("${yearResults.year} Results") {
                    columnSpan = 5
                    alignment = TextAlignment.MiddleCenter
                }
            }
            row("Day", "Part", "Mode", "Answer", "Time")
        }

        footer {
            row {
                cell("Total Time: ") {
                    columnSpan = 4
                }
                cells("${yearResults.runTime}ms")
            }
        }

        yearResults.days.filter { it.isComplete }.forEach { dayResults ->
            row {
                cell(dayResults.day) {
                    rowSpan = 4
                }
                cell("1") {
                    rowSpan = 2
                }
                cells("Sample", dayResults.part1Sample.solution, dayResults.part1Sample.runtime?.let { "${it}ms" })
            }

            row("Real", dayResults.part1Real?.solution ?: "", dayResults.part1Real?.runtime?.let { "${it}ms" } ?: "")

            row {
                cell("2") {
                    rowSpan = 2
                }
                cells(
                    "Sample",
                    dayResults.part2Sample?.solution ?: "",
                    dayResults.part2Sample?.runtime?.let { "${it}ms" } ?: "")
            }
            row("Real", dayResults.part2Real?.solution ?: "", dayResults.part2Real?.runtime?.let { "${it}ms" } ?: "")
        }

    }
}