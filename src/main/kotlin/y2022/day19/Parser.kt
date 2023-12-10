package y2022.day19

import java.nio.file.Files
import java.nio.file.Paths

enum class Resource {
    Ore,
    Clay,
    Obsidian,
    Geode
}

data class Robot(val resource: Resource, val cost: Map<Resource, Int>) {
    val oreCost= cost.getOrDefault(Resource.Ore, 0)
    val clayCost = cost.getOrDefault(Resource.Clay, 0)
    val obsidianCost= cost.getOrDefault(Resource.Obsidian, 0)
}

data class Blueprint(val name: Int, val robotCosts: Map<Resource, Robot>) {
    val oreRobot = robotCosts[Resource.Ore]
    val maxOreRequirement = robotCosts.maxOf { it.value.oreCost }
}

object Parser {
    private val regex =
        "Blueprint (\\d+):.*ore robot costs (\\d+).*clay robot costs (\\d+) .*obsidian robot costs (\\d+) ore and (\\d+) clay.*eode robot costs (\\d+) ore and (\\d+).*".toRegex()

    fun parse(data: List<String>) = data.map { raw ->
        val values = regex.matchEntire(raw)!!.groupValues.drop(1)
        Blueprint(
            name = values[0].toInt(),
            mapOf(
                Resource.Ore to Robot(Resource.Ore, mapOf(Resource.Ore to values[1].toInt())),
                Resource.Clay to Robot(Resource.Clay, mapOf(Resource.Ore to values[2].toInt())),
                Resource.Obsidian to Robot(Resource.Obsidian, mapOf(Resource.Ore to values[3].toInt(), Resource.Clay to values[4].toInt())),
                Resource.Geode to Robot(Resource.Geode, mapOf(Resource.Ore to values[5].toInt(), Resource.Obsidian to values[6].toInt())),
            )
        )
    }
}
