package puzzlerunners.output

import com.jakewharton.picnic.TextAlignment
import com.jakewharton.picnic.table
import puzzlerunners.*

object AsciiTableGenerator {
    fun formatInProgress(yearResults: YearResults) = table {
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
                cells(ExecutionResult.decimalFormatter.format(yearResults.runTime / 1000_000) + "ms")
            }
        }

        println(yearResults)
        println(yearResults.days)
        yearResults.days.filter { it.isComplete }.forEach { dayResults ->
            row {
                cell(dayResults.day) {
                    rowSpan = 4
                }
                cell("1") {
                    rowSpan = 2
                }
                cells("Sample", dayResults.part1Sample.solution, dayResults.part1Sample.formatRuntime())
            }

            row("Real", dayResults.part1Real?.solution ?: "", dayResults.part1Real?.formatRuntime() ?: "")

            row {
                cell("2") {
                    rowSpan = 2
                }
                cells(
                    "Sample",
                    dayResults.part2Sample?.solution ?: "",
                    dayResults.part2Sample?.formatRuntime() ?: ""
                )
            }
            row("Real", dayResults.part2Real?.solution ?: "", dayResults.part2Real?.formatRuntime() ?: "")
        }

    }

    fun formatInProgress(dayResult: DayResults) = table {
        cellStyle {
            border = true
            paddingLeft = 1
            paddingRight = 1
        }

        header {
            row {
                cell("Day ${dayResult.day}/${dayResult.year}") {
                    columnSpan = 4
                    alignment = TextAlignment.MiddleCenter
                }
            }
            row("Part", "Mode", "Answer", "Time", "")
        }

        row {
            cell("1") {
                rowSpan = 2
            }
            cells(
                "Sample",
                dayResult.part1Sample.solution,
                dayResult.part1Sample.formatRuntime(),
                if (dayResult.part1Sample.solution == dayResult.puzzle.part1ExpectedAnswerForSample) {
                    " ✅ "
                } else {
                    "❌ [Expected Result = ${dayResult.puzzle.part1ExpectedAnswerForSample}]"
                }
            )
        }

        row("Real", dayResult.part1Real?.solution ?: "", dayResult.part1Real?.formatRuntime() ?: "")

        if (dayResult.puzzle.part2ExpectedAnswerForSample != NotStarted) {
            row {
                cell("2") {
                    rowSpan = 2
                }
                cells(
                    "Sample",
                    dayResult.part2Sample?.solution,
                    dayResult.part2Sample?.formatRuntime(),
                    when {
                        dayResult.puzzle.part2ExpectedAnswerForSample == NoSampleAnswer -> "❓"
                        dayResult.part2Sample?.solution == dayResult.puzzle.part2ExpectedAnswerForSample -> "✅"
                        else -> "❌ [Expected Result = ${dayResult.puzzle.part2ExpectedAnswerForSample}]"
                    }
                )
            }
            row("Real", dayResult.part2Real?.solution ?: "", dayResult.part2Real?.formatRuntime() ?: "")
        }


        footer {
            row {
                cell("Total Time: ") {
                    columnSpan = 3
                }
                cells(ExecutionResult.decimalFormatter.format(dayResult.totalRuntime / 1000_000) + "ms")
            }
        }
    }
}