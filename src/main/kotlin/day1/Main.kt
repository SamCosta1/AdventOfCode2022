package day1

import java.lang.Integer.max

object Day1Main {
    data class Elf(var calories: MutableList<Int>)
    fun run(): String {
        val elves = mutableListOf(Elf(mutableListOf()))
        Day1Data.sample.map { it.toIntOrNull() }.forEach {
            if (it == null) {
                elves.add(Elf(mutableListOf()))
            } else {
                elves.last().calories.add(it)
            }
        }

        return elves.sortedBy { it.calories.sum() }.takeLast(3).sumBy { it.calories.sum() }.toString()
    }
}