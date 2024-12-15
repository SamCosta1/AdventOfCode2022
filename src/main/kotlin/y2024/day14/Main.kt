package y2024.day14

import PointInt
import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 12,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data, runMode).let { (info, robots) ->
        val t = 100
        val robotPositions = robots.map {
            (it.start + (it.velocity * t)).floorMod(PointInt(info.width, info.height))
        }

        val midX = info.width / 2
        val midY = info.height / 2

        robotPositions.count {
            it.x < midX && it.y < midY  // < <
        } * robotPositions.count {
            it.x < midX && it.y > midY  // < >
        } * robotPositions.count {
            it.x > midX && it.y > midY  // > >
        } * robotPositions.count {
            it.x > midX && it.y < midY //  > <
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
