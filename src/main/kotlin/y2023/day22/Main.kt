package y2023.day22

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.Generic3dGrid
import utils.Point3D
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 5,
    override val part2ExpectedAnswerForSample: Any = 7,
    override val isComplete: Boolean = false,
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (blocks, grid) ->
        simulateFalling(blocks, grid)

        blocks.filter { block ->
            block.points.forEach {
                grid[it] = Parser.GridItem.Air
            }
            blocks.filter { it != block }.all { grid.isSettled(it) }.also {
                block.points.forEach {
                    grid[it] = block
                }
            }
        }.size
    }

    private fun simulateFalling(
        blocks: List<Parser.GridItem.Block>,
        grid: Generic3dGrid<Parser.GridItem>
    ) {
        while (blocks.any { !grid.isSettled(it) }) {
            blocks.forEach { block ->
                if (!grid.isSettled(block)) {
                    block.points.forEach { grid[it] = Parser.GridItem.Air }

                    block.points = block.points.map { it.copy(z = it.z - 1) }

                    block.points.forEach { grid[it] = block }
                }
            }
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Unit /*Parser.parse(data).let { (blocks, grid) ->
        simulateFalling(blocks, grid)

        blocks.sumOf { block ->
            val oldBlocks = blocks.filter { it != block }
            val newBlocks = oldBlocks.map { it.copy() }
            val newGrid = Generic3dGrid<Parser.GridItem>(Parser.GridItem.Air).apply {
                newBlocks.forEach { b ->
                    b.points.forEach { this[it] = b }
                }
            }
            simulateFalling(newBlocks, newGrid)
            newBlocks.mapIndexed { index, b ->
                if (b != oldBlocks[index]) {
                    1
                } else {
                    0
                }
            }.sum()
        }
    }
*/
    fun Generic3dGrid<Parser.GridItem>.isSettled(block: Parser.GridItem.Block): Boolean {
        val comparisonBlocks = if (block.isVertical) listOf(block.points.minBy { it.z }) else block.points
        return comparisonBlocks.any {
            it.z <= 1 || this[it.copy(z = it.z - 1)] is Parser.GridItem.Block
        }
    }

    fun Generic3dGrid<Parser.GridItem>.printX() = buildString {
        val maxZ = point3Ds.keys.maxOf { it.z }
        for (z in (maxZ downTo 0)) {
            for (x in (0..10L)) {
                val y = (0..10L)
                val block = y.map { Point3D(x, it, z) }.firstOrNull {
                    this@printX[it] is Parser.GridItem.Block
                }?.let { this@printX[it] } as? Parser.GridItem.Block ?: Parser.GridItem.Air
                append(block.char)
            }
            appendLine()
        }
    }

    fun Generic3dGrid<Parser.GridItem>.printY() = buildString {
        val maxZ = point3Ds.keys.maxOf { it.z }
        for (z in (maxZ downTo 0L)) {
            for (y in (0..10L)) {
                val x = (0..10L)
                val block = x.map { Point3D(it, y, z) }.firstOrNull {
                    this@printY[it] is Parser.GridItem.Block
                }?.let { this@printY[it] } as? Parser.GridItem.Block ?: Parser.GridItem.Air
                append(block.char)
            }
            appendLine()
        }
    }
}
