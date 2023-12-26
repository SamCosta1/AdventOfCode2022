
import puzzlerunners.NotStarted
import puzzlerunners.output.AsciiTableGenerator
import y2022.Main2022
import y2023.Main2023
import kotlin.system.measureTimeMillis


fun main() {
//    Main2021.runAll()
//    Main2022.executeSmort()
    println(AsciiTableGenerator.formatInProgress(Main2023.run(18)))

}
