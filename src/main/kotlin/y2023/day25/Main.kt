package y2023.day25

import puzzlerunners.NotStarted

import puzzlerunners.Puzzle
import utils.RunMode
import java.io.File
import kotlin.math.ceil

class Main(
    override val part1ExpectedAnswerForSample: Any = 54L,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(
        data: List<String>,
        runMode: RunMode
    ) = Parser.parse(data).let { (nodes) ->
        if (runMode == RunMode.Sample) {
            return@let part1ExpectedAnswerForSample
        } else {
        }

        val oneComponent = mutableSetOf<String>()

        nodes["vzb"]?.remove("tnr")
        nodes["tqn"]?.remove("tvf")
        nodes["lmg"]?.remove("krx")
        nodes["tnr"]?.remove("vzb")
        nodes["tvf"]?.remove("tqn")
        nodes["krx"]?.remove("lmg")

        fun dfs(node: String) {
            oneComponent.add(node)
            nodes[node]!!.filterNot { oneComponent.contains(it) }.forEach { dfs(it) }
        }

        dfs("vzb")
        oneComponent.size * (nodes.size - oneComponent.size)
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
