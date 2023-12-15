package y2023.day13

import puzzlerunners.Puzzle
import utils.RunMode
import utils.safeSubList
import kotlin.math.min

class Main(
    override val part1ExpectedAnswerForSample: Any = 405L,
    override val part2ExpectedAnswerForSample: Any = 400L,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any  = execute(data, tolerance = 0)
    override fun runPart2(data: List<String>, runMode: RunMode): Any = execute(data = data, tolerance = 1)
    private fun execute(data: List<String>, tolerance: Int) = Parser.parse(data).sumOf { pattern ->
        val h = pattern.findHorizontalLineOfReflection("Horizontal", tolerance) * 100L
        val v = if(h == 0L) pattern.rotated90Degres.findHorizontalLineOfReflection("vertical", tolerance) else 0
        h + v
    }

    // Returns index of line
    private fun Parser.Pattern.findHorizontalLineOfReflection(tag: String, tolerance: Int = 0): Int {
        var match = 0
        for (firstBottom in (1 until grid.size)) {
            val topBlock = grid.safeSubList(0, firstBottom)
            val bottomBlock = grid.safeSubList(firstBottom, grid.size)

            val size = min(topBlock.size, bottomBlock.size)

            val diff = topBlock.asReversed().take(size).diff(bottomBlock.take(size))
            if (size > 0 && diff == tolerance) {
                match = firstBottom
                break
            }
        }

        return match
    }
}

private fun List<List<Char>>.diff(other: List<List<Char>>): Int {
    var count = 0
    forEachIndexed { outerIndex, outer ->
        outer.forEachIndexed { innerIndex, inner ->
            if (inner != other[outerIndex][innerIndex]) {
                count +=1
            }
        }
    }

    return count
}
