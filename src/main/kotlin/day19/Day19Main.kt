package day19

class Day19Main(val timeLimit: Int, file: String) {
    val data = Parser.parse(file)

    data class State(
        val robots: Map<Resource, Int>,
        val resourceCount: Map<Resource, Int> = mapOf(
            Resource.Ore to 0,
            Resource.Clay to 0,
            Resource.Obsidian to 0,
            Resource.Geode to 0
        )
    )

    fun run() = data.sumOf { blueprint ->
        computeLargestNumberOfGeodesPossible(
            timeLimit,
            State(mapOf(Resource.Ore to 1, Resource.Clay to 0, Resource.Obsidian to 0, Resource.Geode to 0)),
            blueprint
        ).also {
            println("Blueprint ${blueprint.name}  $it")
        }.resourceCount[Resource.Geode]!! * blueprint.name
    }

    fun runPart2() = data.take(3).productOf { blueprint ->
        computeLargestNumberOfGeodesPossible(
            timeLimit,
            State(mapOf(Resource.Ore to 1, Resource.Clay to 0, Resource.Obsidian to 0, Resource.Geode to 0)),
            blueprint
        ).also {
            println("Blueprint ${blueprint.name}  $it")
        }.resourceCount[Resource.Geode]!!
    }

    private fun computeLargestNumberOfGeodesPossible(timeLimit: Int, startState: State, blueprint: Blueprint): State {
        if (timeLimit == 0) {
            return startState
        }

        val possibleNewStates = startState.possibleNewStates(blueprint)

        return possibleNewStates.filterNotNull().map { buildOption ->
            computeLargestNumberOfGeodesPossible(timeLimit - 1, buildOption, blueprint)
        }.maxByOrNull { it.resourceCount[Resource.Geode]!! }!!
    }

    var a = 0
    private fun State.possibleNewStates(blueprint: Blueprint): Collection<State?> {
        var robotsICanBuild = Resource.values().mapNotNull {
            it.takeUnless {
                blueprint.robotCosts[it]!!.cost.minOf { cost ->
                    resourceCount.getOrDefault(cost.key, 0) / cost.value
                } == 0
            }
        }.filter { resource ->
            if (resource == Resource.Ore) {
                robots[Resource.Ore]!! < blueprint.maxOreRequirement
            } else {
                true
            }
        }

        var addEmptyCase = true
        if (robotsICanBuild.contains(Resource.Geode)) {
            addEmptyCase = false
            robotsICanBuild = listOf(Resource.Geode)
        } else if (robotsICanBuild.contains(Resource.Obsidian)) {
            addEmptyCase = false
            robotsICanBuild = listOf(Resource.Obsidian)
        }

        val newResources = resourceStateAfter1Cycle()
        return robotsICanBuild.map { robot ->
            val robotCost = blueprint.robotCosts[robot]?.cost!!
            State(
                robots.mapValues { it.value + if (it.key == robot) 1 else 0 },
                newResources.mapValues { it.value - robotCost.getOrDefault(it.key, 0) }
            )
        } + State(
            robots,
            newResources
        ).takeIf { addEmptyCase }
//            .also {
//            if (a++ > 30) throw Exception()
//            println("Started with $this  ends with $it")
//        }
    }

    private fun State.resourceStateAfter1Cycle() = resourceCount.toMutableMap().also { resources ->
        Resource.values().forEach { resource ->
            resources[resource] = resources[resource]!! + (robots[resource] ?: 0)
        }
    }.toMap()

}