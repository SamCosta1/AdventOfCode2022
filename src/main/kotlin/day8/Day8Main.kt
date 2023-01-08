package day8

import java.nio.file.Files
import java.nio.file.Paths

object Day8Main {
    data class Tree(val height: Int, val colIndex: Int, val rowIndex: Int, var scenicScore: Int, var isVisible: Boolean)

    val data =
        Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/day8/data.txt")).let { rawList ->
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

    fun run() = data.sumBy { row -> row.count { it.isVisible } }

    fun runPart2() = data.maxOf { row -> row.maxOf { it.scenicScore } }
}