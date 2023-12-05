package y2023.day5

object Parser {
    data class Info(
        val seeds: List<Long>,
        val seedToSoil: SourceToDestinationMap,
        val soilToFert: SourceToDestinationMap,
        val fertToWater: SourceToDestinationMap,
        val waterToLight: SourceToDestinationMap,
        val lightToTemp: SourceToDestinationMap,
        val tempToHum: SourceToDestinationMap,
        val humToLocation: SourceToDestinationMap,
    )

    sealed class Category(open val number: Long) {
        data class Seed(override val number: Long) : Category(number)
        data class Soil(override val number: Long) : Category(number)
        data class Fertilizer(override val number: Long) : Category(number)
        data class Water(override val number: Long) : Category(number)
        data class Light(override val number: Long) : Category(number)
        data class Temperature(override val number: Long) : Category(number)
        data class Humidity(override val number: Long) : Category(number)
        data class Location(override val number: Long) : Category(number)
    }

    data class SourceToDestinationMap(
        private val sortedMaps: List<SourceToDestinationMapItem>
    ) {
        operator fun invoke(source: Long): Long {
            for (map in sortedMaps) {
                if (source < map.sourceStart) {
                    // Gone too far, this map is already too big, so will not find match
                    return source
                }

                if (source < map.sourceEnd) {
                    return map.map(source)
                }
            }

            return source
        }
    }

    data class SourceToDestinationMapItem(
        val sourceStart: Long,
        private val destStart: Long,
        private val rangeLength: Long
    ) {
        val sourceEnd = sourceStart + rangeLength
        fun isInRange(source: Long) = source in sourceStart..(sourceStart + rangeLength)

        fun map(source: Long): Long {
            return destStart + source - sourceStart
        }
    }

    fun parse(data: List<String>): Info {
        val seeds = data.first().removePrefix("seeds: ").split(" ").map { it.toLong() }

        return Info(
            seeds = seeds,
            seedToSoil = parseMap(data.drop(data.indexOf("seed-to-soil map:") + 1)),
            soilToFert = parseMap(data.drop(data.indexOf("soil-to-fertilizer map:") + 1)),
            fertToWater = parseMap(data.drop(data.indexOf("fertilizer-to-water map:") + 1)),
            waterToLight = parseMap(data.drop(data.indexOf("water-to-light map:") + 1)),
            lightToTemp = parseMap(data.drop(data.indexOf("light-to-temperature map:") + 1)),
            tempToHum = parseMap(data.drop(data.indexOf("temperature-to-humidity map:") + 1)),
            humToLocation = parseMap(data.drop(data.indexOf("humidity-to-location map:") + 1)),
        )
    }

    private fun parseMap(data: List<String>) = SourceToDestinationMap(
        data.take(
            data.indexOfFirst { it.isBlank() }.takeUnless { it < 0 } ?: data.size
        ).map { row ->
                val (dest, source, range) = row.split(" ").map { it.toLong() }
                SourceToDestinationMapItem(
                    sourceStart = source,
                    destStart = dest,
                    rangeLength = range
                )
            }.sortedBy { it.sourceStart })
}