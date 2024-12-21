package y2024.day19

import puzzlerunners.Puzzle
import puzzlerunners.NotStarted
import utils.RunMode
import kotlin.math.pow

typealias Combo = Pair<ULong, ULong>

class Main(
    override val part1ExpectedAnswerForSample: Any = 6,
    override val part2ExpectedAnswerForSample: Any = NotStarted,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (towels, combos) ->
        println("no towels ${towels.size}")
        combos.maxOf { combo ->
            towels.filter { combo.contains(it) }.size.also {
//            println("$combo $it")

            }
        }.also { println("max  $it") }
        val bits = 63
        val base = 2.0.pow(bits).toULong()

        println("base ${base.toString(2)} ${base.toString(2).length}")
        println("0    ${(base.shr(0)).toString(2)}")
        println("1    0${(base.shr(1)).toString(2)}")
        println("2    00${(base.shr(2)).toString(2)}")
        println("3    000${(base.shr(3)).toString(2)}")
        println("64   000${(base.shr(63)).toString(2)}")

        var total = 0L
        combos.count { combo ->
            cache.clear()
            val subTowels = towels.filter { combo.contains(it) }
            val subTowelsMap = subTowels.associateWith {
                val index = subTowels.indexOf(it)
                if (index <= 63) {
                    Pair(base.shr(index), 0UL)
                } else {
                    Pair(0UL, base.shr(index - 63))
                }
            }
            val number = numberOfSols(combo, subTowelsMap)?.size ?: 0
            total += number
            println("Number fo sols $combo $number")
            number > 0
        }
            .also {
                println("$runMode $it $total")
            }
    }

    val cache = mutableMapOf<String, Set<Combo>>()

    private fun numberOfSols(combo: String, towels: Map<String, Combo>): Set<Combo>? {
        if (cache[combo] != null) {
            return cache[combo]
        }

        if (combo.length <= 1 && !towels.contains(combo)) {
            return null
        }
        val count = if (towels.contains(combo)) mutableSetOf(towels[combo]!!) else mutableSetOf<Combo>()
        combo.dropLast(1).forEachIndexed { index, c ->
            val firstHalf = numberOfSols(combo.take(index + 1), towels)
            if (!firstHalf.isNullOrEmpty()) {
                val secondHalf = numberOfSols(combo.drop(index + 1), towels)
                firstHalf.forEach { c1 ->
                    secondHalf?.forEach { c2 ->
                        count.add(Pair(c1.first xor c2.first, c1.second xor c2.second))
                    }
                }
            }
        }
        println(count.size)
        cache[combo] = count
        return count
    }

    override fun runPart2(data: List<String>, runMode: RunMode): Any = ""
}
