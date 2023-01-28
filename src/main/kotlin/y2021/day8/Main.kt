package y2021.day8

import utils.Puzzle
import utils.RunMode
import java.lang.Exception
import kotlin.math.pow

class Main : Puzzle {

    val zero = listOf("a", "b", "c", "e", "f", "g")
    val one = listOf("c", "f")
    val two = listOf("a", "c", "d", "e", "g")
    val three = listOf("a", "c", "d", "f", "g")
    val four = listOf("b", "c", "d", "f")
    val five = listOf("a", "b", "d", "f", "g")
    val six = listOf("a", "b", "d", "e", "f", "g")
    val seven = listOf("a", "c", "f")
    val eight = listOf("a", "b", "c", "d", "e", "f", "g")
    val nine = listOf("a", "b", "c", "d", "f", "g")

    val numbers = listOf(zero, one, two, three, four, five, six, seven, eight, nine)

    override fun runPart1(data: List<String>, runMode: RunMode) = data.map {
        it.split("|").last().split(" ")
    }.flatten().count { it.isUnique() }

    private fun newPossiblitiesSet() = "abcdefg".map { it.toString() }.toSet()
    private fun String.isUnique() = trim().length in listOf(3, 2, 7, 4)

    private fun String.containsAllCharsIn(other: String) = other.all { contains(it) }
    override fun runPart2(data: List<String>, runMode: RunMode) = data.sumBy { raw ->
        val combos = raw.split("|").first().split(" ").mapNotNull { it.takeUnless { it.isBlank() } }
            .map { it.toList().sorted().joinToString("") }
        val outputs = raw.split("|").last().split(" ").mapNotNull { it.takeUnless { it.isBlank() } }
            .map { it.toList().sorted().joinToString("") }

        val results = mutableMapOf<Int, String>()
        combos.forEach { combo ->
            if (combo.isUnique()) {
                results[numbers.indexOfFirst { it.size == combo.length }] = combo
            }
        }

        combos.filter { it.length == 6 }.forEach { combo ->
            if (!combo.containsAllCharsIn(results.getOrDefault(1, "X")) && !combo.containsAllCharsIn(results.getOrDefault(7, "X"))) {
                results[6] = combo
            } else if (combo.containsAllCharsIn(results.getOrDefault(4, "X"))) {
                results[9] = combo
            } else {
                results[0] = combo
            }
        }

        // c maps to this character
        val c = results[0]!!.toList().filter { !results[6]!!.contains(it) }.joinToString("")
        val e = results[0]!!.toList().filter { !results[9]!!.contains(it) }.joinToString("")

        combos.filter { it.length == 5 }.forEach { combo ->
            if (!combo.contains(c)) {
                results[5] = combo
            } else if (combo.contains(e)){
                results[2] = combo
            } else results[3] = combo
        }

        val number = outputs.reversed().mapIndexed { index, s ->
            10.0.pow(index) * results.filter { it.value == s }.keys.first()
        }.sum()

        number.toInt()
    }
}
