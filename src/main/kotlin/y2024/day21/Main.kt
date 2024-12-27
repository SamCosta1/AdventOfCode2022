package y2024.day21

import PointInt
import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.MovementDirection
import utils.RunMode


class Main(
    override val part1ExpectedAnswerForSample: Any = 126384L,
    override val part2ExpectedAnswerForSample: Any = 23,
    override val isComplete: Boolean = true
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = data.sumOf {
        it.removeSuffix("A").toLong() * shortestSequence("A$it")
    }

    private fun shortestSequence(input: String): Long {
        val shortestKeyPadPaths = precomputeNumericalKeypadPaths()
        val shortestDirectionalPaths = precomputeDirectionalKeypadPaths()

        input.dropLast(1).forEachIndexed { index, start ->
            val end = input[index + 1]
            val paths = shortestKeyPadPaths[start to end]!!
            println("start $start end $end ${paths.map { it.map { it.char } }}")
            paths.forEach { path ->
                nextLayer(path.map { it.char } + listOf('A'),  shortestDirectionalPaths)
            }
        }
        return 0
    }

    fun nextLayer(
        buttonsThatNeedToBePressed: List<Char>,
        shortestDirectionalPaths: Map<Pair<Char, Char>, List<List<MovementDirection>>>
    ) {
        val pathNeeded = listOf('A') + buttonsThatNeedToBePressed
        println("Buttons to press: $buttonsThatNeedToBePressed, path to do so $pathNeeded")

        pathNeeded.dropLast(1).forEachIndexed { index, direction ->
            val start = direction
            val end = buttonsThatNeedToBePressed[index + 1]

            shortestDirectionalPaths[start to end]!!

        }
    }
    private fun precomputeDirectionalKeypadPaths(): Map<Pair<Char, Char>, List<List<MovementDirection>>> {
        val keyToPoint = mutableMapOf<PointInt, Char>()
        val keypad = """
             ^A
            <v>
        """.trimIndent().lines()
        keypad.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                when {
                    c == ' ' -> Unit
                    else -> keyToPoint[PointInt(x, y)] = c

                }
            }
        }

        fun search(current: PointInt, target: PointInt, soFar: List<PointInt>, results: MutableList<List<PointInt>>) {
            if (current == target) {
                results.add(soFar)
                return
            }

            current.adjacentNoDiagonal().filter { !soFar.contains(it) && keyToPoint.contains(it) }.forEach {
                search(it, target, soFar + listOf(it), results)
            }
        }

        val results = mutableMapOf<Pair<Char, Char>, List<List<MovementDirection>>>()
        keyToPoint.keys.forEach { p1 ->
            keyToPoint.keys.forEach { p2 ->
                if (p1 != p2) {
                    val thisResult = mutableListOf<List<PointInt>>()
                    search(p1, p2, listOf(p1), thisResult)
                    val smallest = thisResult.minOf { it.size }
                    val withDirs = thisResult.filter { it.size == smallest }.map { path ->
                        val dirs = mutableListOf<MovementDirection>()
                        path.dropLast(1).forEachIndexed { index, point ->
                            dirs.add(MovementDirection.entries.first { path[index + 1] == point(it) })
                        }
                        dirs
                    }
                    results[keyToPoint[p1]!! to keyToPoint[p2]!!] = withDirs
                }
            }
        }
        return results
    }

    private fun precomputeNumericalKeypadPaths(): Map<Pair<Char, Char>, List<List<MovementDirection>>> {
        val keyToPoint = mutableMapOf<PointInt, Char>()
        val keypad = """
            789
            456
            123
             0A
        """.trimIndent().lines()
        keypad.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c != ' ') {
                    keyToPoint[PointInt(x, y)]  = c
                }
            }
        }

        fun search(current: PointInt, target: PointInt, soFar: List<PointInt>, results: MutableList<List<PointInt>>) {
            if (current == target) {
                results.add(soFar)
                return
            }

            current.adjacentNoDiagonal().filter { !soFar.contains(it) && keyToPoint.contains(it) }.forEach {
                search(it, target, soFar + listOf(it), results)
            }
        }

        val results = mutableMapOf<Pair<Char, Char>, List<List<MovementDirection>>>()
        keyToPoint.keys.forEach { p1 ->
            keyToPoint.keys.forEach { p2 ->
                if (p1 != p2) {
                    val thisResult = mutableListOf<List<PointInt>>()
                    search(p1, p2, listOf(p1), thisResult)
                    val smallest = thisResult.minOf { it.size }
                    val withDirs = thisResult.filter { it.size == smallest }.map { path ->
                        val dirs = mutableListOf<MovementDirection>()
                        path.dropLast(1).forEachIndexed { index, point ->
                            dirs.add(MovementDirection.entries.first { path[index + 1] == point(it) })
                        }
                        dirs
                    }
                    results[keyToPoint[p1]!! to keyToPoint[p2]!!] = withDirs
                }
            }
        }
        return results
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
