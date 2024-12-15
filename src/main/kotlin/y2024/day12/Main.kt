package y2024.day12

import PointInt
import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 1930L,
    override val part2ExpectedAnswerForSample: Any = 1206L,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = calculatePriceForRegions(Parser.parse(data), 1)

    private fun calculatePriceForRegions(map: Array<CharArray>, part: Int): Long {
        var currentRoot: PointInt? = PointInt(1, 1)
        var total = 0L
        while (currentRoot != null) {
            total += calculatePriceForRegion(currentRoot, map, part)
            currentRoot = findNextRoot(map)
        }
        return total
    }

    operator fun Array<CharArray>.get(point: PointInt) = this[point.y][point.x]

    private fun calculatePriceForRegion(currentRoot: PointInt, map: Array<CharArray>, part: Int): Long {
        val currentRegion = mutableSetOf(currentRoot)
        val regionSymbol = map[currentRoot.y][currentRoot.x]
        val perimeterNodes = mutableSetOf(currentRoot)
        var perimeterLength = 0L

        fun regionSearch(pos: PointInt) {
            val neighbours = pos.adjacentNoDiagonal().filter { !currentRegion.contains(it) }
            val neighboursInRegion = neighbours.filter { map[it] == regionSymbol }
            val neighboursNotInRegion = neighbours.filter { map[it] == regionSymbol }
            perimeterLength += neighbours.size - neighboursInRegion.size
            perimeterNodes += neighboursNotInRegion

            neighboursInRegion.forEach {
                currentRegion += it
            }

            neighboursInRegion.forEach {
                regionSearch(it)
            }
        }

        regionSearch(currentRoot)
        val perimeterOrSides = if (part == 1) perimeterLength else countSides(regionSymbol, perimeterNodes, map)
        currentRegion.forEach { pos -> map[pos.y][pos.x] = '@' }
        return perimeterOrSides * currentRegion.size
    }

    private fun countSides(thisSymbol: Char, perimeterNodes: MutableSet<PointInt>, map: Array<CharArray>): Long {


        val minY = perimeterNodes.minOf { it.y } - 2
        val maxY = perimeterNodes.maxOf { it.y } + 2
        val minX = perimeterNodes.minOf { it.x } - 2
        val maxX = perimeterNodes.maxOf { it.x } + 2

        var horizontal = 0L
        var vertical = 0L
        (minY..maxY).forEach { y ->
            val topEdges = mutableListOf<Int>()
            val bottomEdges = mutableListOf<Int>()
            (minX..maxX).forEach { x ->
                val thisPoint = PointInt(x, y)
                // Top Edge
                topEdges.addAll(perimeterNodes.filter { it.top == thisPoint && map[thisPoint] != thisSymbol }
                    .map { it.x })

                // Bottom Edge
                bottomEdges.addAll(perimeterNodes.filter { it.bottom == thisPoint && map[thisPoint] != thisSymbol }
                    .map { it.x })
            }
            horizontal += countRanges(topEdges) + countRanges(bottomEdges)
        }

        (minX..maxX).forEach { x ->
            val leftEdges = mutableListOf<Int>()
            val righEdges = mutableListOf<Int>()
            (minY..maxY).forEach { y ->
                val thisPoint = PointInt(x, y)
                // Left Edge
                leftEdges.addAll(perimeterNodes.filter { it.left == thisPoint && map[thisPoint] != thisSymbol }
                    .map { it.y })

                // Right Edge
                righEdges.addAll(perimeterNodes.filter { it.right == thisPoint && map[thisPoint] != thisSymbol }
                    .map { it.y })
            }
            vertical += countRanges(leftEdges) + countRanges(righEdges)
        }

        return horizontal + vertical
    }

    private fun countRanges(edgePoints: MutableList<Int>): Long {
        if (edgePoints.size == 0) {
            return 0
        }

        edgePoints.sort()
        var count = 1L
        for (i in 0..<edgePoints.lastIndex) {
            if (edgePoints[i] + 1 != edgePoints[i + 1]) {
                count++
            }
        }
        return count
    }

    private fun findNextRoot(map: Array<CharArray>): PointInt? {
        var newX: Int? = null
        val newY = map.indexOfFirst { row ->
            row.indexOfFirst { it != '@' }.also { newX = it } != -1
        }
        return if (newX != null && newY != -1) {
            PointInt(newX!!, newY)
        } else {
            null
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = calculatePriceForRegions(Parser.parse(data), 2)
}
