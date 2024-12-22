import puzzlerunners.NewYearCreator
import puzzlerunners.NotStarted
import puzzlerunners.output.AsciiGraphGenerator
import puzzlerunners.output.AsciiTableGenerator
import y2022.Main2022
import y2023.Main2023
import y2024.Main2024
import kotlin.system.measureTimeMillis


fun main() {
//    println(AsciiTableGenerator.formatInProgress(yearResults))
//    println(AsciiTableGenerator.formatInProgress(Main2024.runAll()))
//    println(AsciiGraphGenerator.formatGraph(Main2024.runAll()))

    println(AsciiTableGenerator.formatInProgress(Main2024.run(17)))
//    println(AsciiTableGenerator.formatInProgress(Main2024.runLatest()))
}
