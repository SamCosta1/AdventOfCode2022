package y2021.day8

import utils.Puzzle
import utils.RunMode
import java.lang.Exception

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

    private fun newPossiblitiesSet() = "abcdefg".map { it.toString() }.toMutableSet()
    private fun String.isUnique() = trim().length in listOf(3, 2, 7, 4)
    override fun runPart2(data: List<String>, runMode: RunMode) = data.map { raw ->
        val combos = raw.split("|").first().split(" ").mapNotNull { it.takeUnless { it.isBlank() } }
        val outputs = raw.split("|").last().split(" ").mapNotNull { it.takeUnless { it.isBlank() } }

        val possibilities = "abcdefg".map { it.toString() }.map { it to newPossiblitiesSet() }.toMap().toMutableMap()
        var comboIndex = 0

        combos.forEach { combo ->
            val match = numbers.filter { it.size == combo.length }.flatten()
            combo.map { it.toString() }.forEach { character ->
//                println(
//                    "$character current possibilities : ${possibilities[character]} match: $match  new: ${
//                        possibilities[character]!!.intersect(
//                            match
//                        ).toMutableSet()
//                    }"
//                )
                possibilities[character] = possibilities[character]!!.intersect(match).toMutableSet()

            }
        }

        println("After step 1: ${possibilities}")

        combos.filter { it.length == 6 }.forEach { combo ->

        }

        if (possibilities.values.any { it.size != 1 }) {
            throw Exception("Something bad happened")
        }
    }
}
