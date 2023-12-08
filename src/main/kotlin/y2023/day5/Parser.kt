package y2023.day5

import java.lang.Long.max
import kotlin.math.min

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
    ) {
        val orderedMaps = listOf(
            seedToSoil,
            soilToFert,
            fertToWater,
            waterToLight,
            lightToTemp,
            tempToHum,
            humToLocation,
        )
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

        operator fun invoke(source: List<LongRange>): List<LongRange> {
            return source.map { invoke(it) }.flatten()
        }

        operator fun invoke(source: LongRange): List<LongRange> {
            val result = mutableListOf<LongRange>()
            var currentRange = source


            for (map in sortedMaps) {
                println("map ${map.sourceStart}..${map.sourceEnd} current $currentRange")
                if (currentRange.first < map.sourceStart) {
                    result += (currentRange.first..min(currentRange.last, map.sourceStart - 1))
                }
                if (currentRange.last < map.sourceStart) {
                    break
                }
                if (currentRange.start > map.sourceEnd) {
                    continue
                }

                println("result1 $result")
                val endOfWhatThisMapCanHandle = min(currentRange.last, map.sourceEnd)
                result += (map.map(max(currentRange.first, map.sourceStart))..map.map(endOfWhatThisMapCanHandle))
                println("result2 $result")

                currentRange = ((endOfWhatThisMapCanHandle + 1)..currentRange.last)

                if (currentRange.isEmpty()) {
                    break
                }
            }

            if (currentRange.first > sortedMaps.last().sourceEnd) {
                result += ((sortedMaps.last().sourceEnd + 1)..currentRange.first)
            }

            println("Source $source result $result")
            return result.filter { !it.isEmpty() }
//            val mapContainingSourceStart = sortedMaps.firstOrNull { it.isInRange(source.first) }
//
//            if (mapContainingSourceStart == null) {
//                val mapBiggerThanSource = sortedMaps.firstOrNull {
//                    it.sourceStart >= source.first
//                }?.takeIf { it.isInRange(source.first) } ?: return listOf(source)
//
//                result += (source.first..min(source.last, mapBiggerThanSource.sourceEnd))
//            }

        }
    }

    data class SourceToDestinationMapItem(
        val sourceStart: Long,
        private val destStart: Long,
        private val rangeLength: Long
    ) {
        val sourceEnd = sourceStart + rangeLength
        fun isInRange(source: Long) = source in sourceStart until sourceStart + rangeLength

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