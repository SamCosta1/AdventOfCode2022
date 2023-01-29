package y2021.day11

import utils.Point
import utils.Puzzle
import utils.RunMode

class Main: Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->

        val allPoints = grid.points.keys
        val allOctopuses = grid.points.values

        repeat(100) { index ->
            val alreadyFlashed = mutableSetOf<Point>()
            allOctopuses.forEach { it.energy = it.energy + 1 }

            allPoints.forEach { simulate(it, grid, alreadyFlashed) }

            alreadyFlashed.forEach {
                grid.points[it]!!.energy = 0
            }
        }

        grid.points.values.sumOf { it.numFlashes }
    }

    private fun simulate(point: Point, grid: Grid, alreadyFlashed: MutableSet<Point>) {
        val octopus = grid.points.get(point) ?: return
        if (octopus.energy <= 9 || alreadyFlashed.contains(point)) {
            return
        }

        alreadyFlashed.add(point)
        octopus.numFlashes = octopus.numFlashes + 1

        grid.adjacentNodes(point.x, point.y).forEach {
            val adjacentOctopus = grid.points[it]
            adjacentOctopus!!.energy++

            simulate(it, grid, alreadyFlashed)
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->

        val allPoints = grid.points.keys
        val allOctopuses = grid.points.values

        var step = 0
        while (allOctopuses.any { it.energy > 0 }) {
            val alreadyFlashed = mutableSetOf<Point>()
            allOctopuses.forEach { it.energy = it.energy + 1 }

            allPoints.forEach { simulate(it, grid, alreadyFlashed) }

            alreadyFlashed.forEach {
                grid.points[it]!!.energy = 0
            }

            step++
        }

        step
    }
}
