package y2022.day8

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 21,
    override val part2ExpectedAnswerForSample: Any = 8,
    override val isComplete: Boolean = true
): Puzzle {
    data class Tree(val height: Int, val colIndex: Int, val rowIndex: Int, var scenicScore: Int, var isVisible: Boolean)

    fun parse(data: List<String>) = data.let { rawList ->
            val structure = Array(rawList.size) { Array<Tree>(rawList.first().length) { Tree(-1, -1, 1, -1, false) } }

            rawList.forEachIndexed { index, row ->
                row.forEachIndexed { colIndex, char ->
                    // Initialise everything to not be visible, once it's set to visible it can't be un-visibled
                    structure[index][colIndex] = Tree(char.toString().toInt(), colIndex, index, -1, false)
                }
            }

            computeVisibilities(structure)
            structure.forEach { row ->
                row.forEach {
                    it.scenicScore = computeViewDistance(structure, it)
                }
            }

            structure
        }

    private fun computeViewDistance(data: Array<Array<Tree>>, tree: Tree): Int {
        val leftVD = tree.computeVd(((tree.colIndex - 1) downTo 0).map { data[tree.rowIndex][it] })
        val rightVD = tree.computeVd((tree.colIndex + 1 until data.first().size).map { data[tree.rowIndex][it] })
        val topVD = tree.computeVd(((tree.rowIndex - 1) downTo 0).map { data[it][tree.colIndex] })
        val bottomVD = tree.computeVd(((tree.rowIndex + 1) until data.size).map { data[it][tree.colIndex] })

        return leftVD * rightVD * topVD * bottomVD
    }

    private fun Tree.computeVd(viewOrder: List<Tree>): Int {

        var count = 0
        for (t in viewOrder) {
            count++
            if (t.height >= this.height) {
                break
            }
        }
        return count
    }

    private fun computeVisibilities(structure: Array<Array<Tree>>) {
        // Each Row L -> R
        structure.forEach { row ->
            var currentHeight = -1
            row.forEach { tree ->
                if (tree.height > currentHeight) {
                    tree.isVisible = true
                }
                currentHeight = maxOf(tree.height, currentHeight)
            }
        }

        // Each Row R -> L
        structure.forEach { row ->
            var currentHeight = -1
            row.reversedArray().forEach { tree ->
                if (tree.height > currentHeight) {
                    tree.isVisible = true
                }
                currentHeight = maxOf(tree.height, currentHeight)
            }
        }

        (0 until structure.first().size).forEach { column ->
            var currentHeight = -1

            structure.indices.forEach { row ->
                val tree = structure[row][column]
                if (tree.height > currentHeight) {
                    tree.isVisible = true
                }
                currentHeight = maxOf(tree.height, currentHeight)
            }
        }

        (0 until structure.first().size).forEach { column ->
            var currentHeight = -1

            structure.indices.reversed().forEach { row ->
                val tree = structure[row][column]
                if (tree.height > currentHeight) {
                    tree.isVisible = true
                }
                currentHeight = maxOf(tree.height, currentHeight)
            }
        }
    }

    override fun runPart1(data: List<String>, runMode: RunMode) = parse(data).sumBy { row -> row.count { it.isVisible } }
    override fun runPart2(data: List<String>, runMode: RunMode) = parse(data).maxOf { row -> row.maxOf { it.scenicScore } }
}