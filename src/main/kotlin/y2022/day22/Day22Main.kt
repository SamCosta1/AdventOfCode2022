package y2022.day22

import y2022.day15.Point

sealed class Surface {
    object FlatMap : Surface()
    data class Cube(val size: Int) : Surface()
}

class Day22Main(val file: String) {

    fun runPart2(size: Int): Long {
        val data = Parser.parse(file)

//        (1..4).forEach { y ->
//            data.grid.rowMetaData[y] = data.grid.rowMetaData[y]!!.copy(
//                firstTeleport = Grid.CubeOverflow(Point(4L + y, 5), Direction.Down),
//                lastTeleport = Grid.CubeOverflow(Point(1, 9L + 4  - y ), Direction.Left),
//            )
//        }
//
//        (5..8).forEach { y ->
//            data.grid.rowMetaData[y] = data.grid.rowMetaData[y]!!.copy(
//                firstTeleport = Grid.CubeOverflow(Point(17L + 4  - (y - 4), 12), Direction.Up),
//                lastTeleport = Grid.CubeOverflow(Point(17L + 4  - (y - 4), 12), Direction.Down),
//            )
//        }
//
//        (9..12).forEach { y ->
//            data.grid.rowMetaData[y] = data.grid.rowMetaData[y]!!.copy(
//                firstTeleport = Grid.CubeOverflow(Point(9L  - (y - 8), 8), Direction.Up),
//                lastTeleport = Grid.CubeOverflow(Point(12, 9L - (y - 4)), Direction.Left),
//            )
//        }
//
//        (1..4).forEach { x ->
//            data.grid.columnMetaData[x] = data.grid.columnMetaData[x]!!.copy(
//                firstTeleport = Grid.CubeOverflow(Point(8L + (4 - x), 1), Direction.Down),
//                lastTeleport = Grid.CubeOverflow(Point(13L - x, 12), Direction.Up),
//            )
//        }

        // Rows
        (1..50).forEach { y ->
            data.grid.rowMetaData[y] = data.grid.rowMetaData[y]!!.copy(
                firstTeleport = Grid.CubeOverflow(Point(1, 151L - y), Direction.Right),
                lastTeleport = Grid.CubeOverflow(Point(100, 151L - y), Direction.Left),
            )
        }

        (51..100).forEach { y ->
            data.grid.rowMetaData[y] = data.grid.rowMetaData[y]!!.copy(
                firstTeleport = Grid.CubeOverflow(Point(y - 50L, 101), Direction.Down),
                lastTeleport = Grid.CubeOverflow(Point(y + 50L, 50), Direction.Up),
            )
        }

        (101..150).forEach { y ->
            data.grid.rowMetaData[y] = data.grid.rowMetaData[y]!!.copy(
                firstTeleport = Grid.CubeOverflow(Point(51, 151L - y), Direction.Right),
                lastTeleport = Grid.CubeOverflow(Point(150L, 151L - y), Direction.Left),
            )
        }

        (151..200).forEach { y ->
            data.grid.rowMetaData[y] = data.grid.rowMetaData[y]!!.copy(
                firstTeleport = Grid.CubeOverflow(Point(y - 100L,1), Direction.Down),
                lastTeleport = Grid.CubeOverflow(Point(y - 100L, 150L), Direction.Up),
            )
        }

        // Columns
        (1..50).forEach {  x ->
            data.grid.columnMetaData[x] = data.grid.columnMetaData[x]!!.copy(
                firstTeleport = Grid.CubeOverflow(Point(51, x + 50L), Direction.Right),
                lastTeleport = Grid.CubeOverflow(Point(x + 100L, 1L), Direction.Down),
            )
        }

        (51..100).forEach { x ->
            data.grid.columnMetaData[x] = data.grid.columnMetaData[x]!!.copy(
                firstTeleport = Grid.CubeOverflow(Point(1, x + 100L), Direction.Right),
                lastTeleport = Grid.CubeOverflow(Point(50, x + 100L), Direction.Left),
            )
        }

        (101..150).forEach { x ->
            data.grid.columnMetaData[x] = data.grid.columnMetaData[x]!!.copy(
                firstTeleport = Grid.CubeOverflow(Point(x - 100L, 200L), Direction.Up),
                lastTeleport = Grid.CubeOverflow(Point(100, x - 50L), Direction.Left),
            )
        }


        return run(data, Surface.Cube(size)).also {
            println("Teleports $count")
            println("Column Metas:")
            columnMetaUsed.forEach {
                println("${it.value.coord} ${it.value.firstTeleport} ${it.value.lastTeleport}")
            }
            println("Row Metas:")
            rowMetaUsed.forEach {
                println(it)
            }
        }
    }

    fun run(data: Data = Parser.parse(file), surface: Surface = Surface.FlatMap): Long {
        val grid = data.grid

        var currentPosition = data.startPoint
        var currentDirection = Direction.Right

        grid.debugPoints.put(currentPosition, 'S')
        data.instructions.forEach { instruction ->
            val new = computeNextPosition(surface, currentPosition, currentDirection, instruction, grid)
            currentPosition = new.first
            currentDirection = new.second

            grid.debugPoints[currentPosition] = currentDirection.char
        }

//        println(grid)
        println("Final position " + currentPosition)
        return (1000 * currentPosition.y) + (4 * currentPosition.x) + when (currentDirection) {
            Direction.Right -> 0
            Direction.Down -> 1
            Direction.Left -> 2
            Direction.Up -> 3
        }
    }

    private fun computeNextPosition(
        surface: Surface,
        currentPosition: Point,
        startDirection: Direction,
        instruction: Instruction,
        grid: Grid
    ) = when (instruction) {
        is Instruction.Rotate -> {
            Pair(currentPosition, startDirection.afterApplying(instruction.newDirection))
        }

        is Instruction.Move -> {
            var steps = 1
            var current = currentPosition
            var currentDirection = startDirection
            while (steps <= instruction.amount) {
                val next = when (surface) {
                    Surface.FlatMap -> takeOneStepFlat(current, currentDirection, grid)
                    is Surface.Cube -> takeOneStepCube(current, currentDirection, grid)
                }
                //            println("Current pos $current instruction $instruction $nextPosition, ${grid[nextPosition]}")
                when (grid[next.first]) {
                    Grid.State.Rock -> break // This move is invalid, we've done the last valid move
                    Grid.State.Open -> {
                        current = next.first // all good, keep going
                        currentDirection = next.second
                    }
                    else -> throw Exception("Moving once should never take us off the map from: ${current} to ${next.first} $currentDirection")
                }
                steps++
            }
            Pair(current, currentDirection)
        }
    }

    var count = 0
    val rowMetaUsed = mutableMapOf<Long, Grid.RowColumnMetaData>()
    val columnMetaUsed = mutableMapOf<Long, Grid.RowColumnMetaData>()

    private fun takeOneStepCube(fromPosition: Point, direction: Direction, grid: Grid): Pair<Point, Direction> {
        return if (direction == Direction.Right || direction == Direction.Left) {
            (fromPosition.x + when (direction) {
                Direction.Right -> 1L
                Direction.Left -> -1L
                else -> throw Exception("Eh")
            }).let { rawX ->
                val rowMetaData = grid.rowMetaData[fromPosition.y.toInt()]!!
//                println("RawX $rawX fromPos: $fromPosition rowMeta $rowMetaData")
                when {
                    rawX < rowMetaData.firstRockOrOpen -> rowMetaData.firstTeleport!!.toPair().also { count++; rowMetaUsed.put(fromPosition.y / 50, rowMetaData) }
                    rawX > rowMetaData.lastRockOrOpen -> rowMetaData.lastTeleport!!.toPair().also { count++; rowMetaUsed.put(fromPosition.y / 50, rowMetaData) }
                    else -> Pair(Point(rawX, fromPosition.y), direction)
                }
            }
        } else {
            (fromPosition.y + when (direction) {
                Direction.Down -> 1L
                Direction.Up -> -1L
                else -> throw Exception("Eh")
            }).let { rawY ->
                val columnMetaData = grid.columnMetaData[fromPosition.x.toInt()]!!
                when {
                    rawY < columnMetaData.firstRockOrOpen -> columnMetaData.firstTeleport!!.toPair().also { count++; columnMetaUsed.put(fromPosition.x / 50, columnMetaData) }
                    rawY > columnMetaData.lastRockOrOpen -> columnMetaData.lastTeleport!!.toPair().also { count++; columnMetaUsed.put(fromPosition.x / 50, columnMetaData) }
                    else -> Pair(Point(fromPosition.x, rawY), direction)
                }
            }
        }
    }

    private fun takeOneStepFlat(fromPosition: Point, direction: Direction, grid: Grid): Pair<Point, Direction> {
        return if (direction == Direction.Right || direction == Direction.Left) {
            val newX = (fromPosition.x + when (direction) {
                Direction.Right -> 1L
                Direction.Left -> -1L
                else -> throw Exception("Eh")
            }).let { rawX ->
                val rowMetaData = grid.rowMetaData[fromPosition.y.toInt()]!!
//                println("RawX $rawX fromPos: $fromPosition rowMeta $rowMetaData")
                when {
                    rawX < rowMetaData.firstRockOrOpen -> rowMetaData.lastRockOrOpen
                    rawX > rowMetaData.lastRockOrOpen -> rowMetaData.firstRockOrOpen
                    else -> rawX
                }
            }
            Pair(fromPosition.copy(x = newX), direction)
        } else {
            val newY = (fromPosition.y + when (direction) {
                Direction.Down -> 1L
                Direction.Up -> -1L
                else -> throw Exception("Eh")
            }).let { rawY ->
                val columnMetaData = grid.columnMetaData[fromPosition.x.toInt()]!!
                when {
                    rawY < columnMetaData.firstRockOrOpen -> columnMetaData.lastRockOrOpen
                    rawY > columnMetaData.lastRockOrOpen -> columnMetaData.firstRockOrOpen
                    else -> rawY
                }
            }
//            println("Old $fromPosition direction $direction newY=$newY")
            Pair(fromPosition.copy(y = newY), direction)
        }
    }
}