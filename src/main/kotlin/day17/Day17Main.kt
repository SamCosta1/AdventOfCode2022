package day17

import day15.Point

class Day17Main(file: String) {
    val grid = Grid(7)
    val data = Parser.parse(file)

    data class TopProfile(val indicies: List<Int>, val currentRockIndex: Int, val dataIndex: Int)
    data class TopProfileInfo(val topY: Long, val timeStep: Long, val currentTotalRocks: Long)

    fun run(totalRocks: Long): Long {
        val rocks = Rock.ordered

        var timeStep = 0L
        var numberOfRocks = 0L

        val topProfiles = mutableMapOf<TopProfile, TopProfileInfo>()

        while (true) { // Iterates once per rock
            val rockIndex = (numberOfRocks % rocks.size).toInt()
            val rock = rocks[rockIndex]
            timeStep = simulateMotion(rocks, rock, timeStep)

            numberOfRocks++


            val last4 = (grid.topOfTowerYIndex downTo grid.topOfTowerYIndex - 3).map { y ->
                (0..6L).map { x -> grid[x, y] }
            }
            val tallestRockIndicies = (0..6).map { x ->
                val inverseIndex = (0..3).firstOrNull { y -> last4[y][x] == Grid.State.Rock  }
                inverseIndex?.let { 3 - it }
            }

            if (numberOfRocks == totalRocks) {
                break
            }

            if (tallestRockIndicies.all { it != null }) {
                val topProfile = TopProfile(tallestRockIndicies.map { it!! }, rockIndex, (timeStep % data.size).toInt())

                val previousMatch = topProfiles[topProfile]
                if (previousMatch != null) {
                    topProfiles.clear() // Don't want to interfere with everything else

                    val rocksInOneCycle = numberOfRocks - previousMatch.currentTotalRocks
                    val remainingCycles = (totalRocks - numberOfRocks) / rocksInOneCycle
                    val remainder = totalRocks - (numberOfRocks + (rocksInOneCycle * remainingCycles))

                    val cycleHeight = grid.topOfTowerYIndex - previousMatch.topY
                    val skipToY = remainingCycles * cycleHeight + grid.topOfTowerYIndex

                    (0..3).forEach { yDelta ->
                        (0..6).forEach { x ->
                            if (topProfile.indicies[x] == 3 - yDelta) {
                                grid[Point(x.toLong(), skipToY - yDelta)] = Grid.State.Rock
                            }
                        }
                    }

                    numberOfRocks += rocksInOneCycle * remainingCycles
                    timeStep += remainingCycles * (timeStep - previousMatch.timeStep)
                }

                topProfiles[topProfile] = TopProfileInfo(grid.topOfTowerYIndex, timeStep = timeStep, numberOfRocks)
            }
        }

        return grid.topOfTowerYIndex + 1
    }

    private fun simulateMotion(
        rocks: List<Rock>,
        rock: Rock,
        startDataIndex: Long,
    ): Long {
        var dataIndex = startDataIndex
        val emissionPoint = Point(2, rock.height() + grid.topOfTowerYIndex + 3)
        var currentTopLeft = emissionPoint

        while (true) {
            // Do Horizontal Movement if possible
            val jetDirection = data[(dataIndex++ % data.size).toInt()]
            var nextTopLeft = currentTopLeft.apply(jetDirection)

            if (grid.canMoveTo(nextTopLeft, rock)) {
                currentTopLeft = nextTopLeft
            }

            // Do downward movement
            nextTopLeft = currentTopLeft.copy(y = currentTopLeft.y - 1)
            if (grid.canMoveTo(nextTopLeft, rock)) {
                currentTopLeft = nextTopLeft
            } else {
                break
            }
        }

        grid.addRock(currentTopLeft, rock)

        return dataIndex
    }

    fun runPart2() = ""
}