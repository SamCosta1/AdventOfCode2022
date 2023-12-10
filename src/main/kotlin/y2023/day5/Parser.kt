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
            return source.map { invoke(it) }.flatten().toSet().toList()
        }

        operator fun invoke(source: LongRange): List<LongRange> {
            val result = mutableListOf<LongRange>()
            var currentRange = source

            val addResult = { range: LongRange ->
                if (!range.isEmpty()) {
                    result += range
                }
            }

            for (map in sortedMaps) {
                if (currentRange.isEmpty()) {
                    break
                }
                // Add the bit up to the start of this map
                addResult(currentRange.coerceAtMost(map.sourceStart-1))

                // If that's the entire range we're done
                if (currentRange.last < map.sourceStart) {
                    break
                }

                // Add the bit that overlaps this map (and actually do the conversion
                val overlapping = currentRange.coerceAtLeast(map.sourceStart).coerceAtMost(map.sourceEnd -1)
                addResult(map.map(overlapping.first)..map.map(overlapping.last))

                // Update the range to start after this map finishes
                currentRange = currentRange.coerceAtLeast(map.sourceEnd)
            }

            addResult(currentRange)
            return result.filter { !it.isEmpty() }
        }
    }

    private fun LongRange.coerceAtMost(value: Long) = (first..min(last, value))
    private fun LongRange.coerceAtLeast(value: Long) = (max(first, value)..last)

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

        override fun toString(): String {
            return "$sourceStart->$sourceEnd : $destStart -> ${map(sourceEnd)}"
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