package day25

import kotlin.math.pow

class Day25Main(val file: String) {

    fun run(): String {
        val inputs = Parser.parse(file)

        val sumDecimal = inputs.sumOf { it.toDecimal() }
        println(sumDecimal)
        return sumDecimal.toSnafu().toString()
    }

    fun Long.toSnafu(): Parser.Snafu {
        var current = this
        val digits = mutableListOf<Int>()

        while (current != 0L) {
            val divisor = current / 5
            val remainder = current % 5

            current = if (remainder >= 3) {
                digits.add(remainder.toInt() - 5)
                divisor + 1
            } else {
                digits.add(remainder.toInt())
                divisor
            }
        }

        return Parser.Snafu(digits.reversed())
    }

    fun Parser.Snafu.toDecimal(): Long = digits
        .reversed()
        .mapIndexed { index, number ->
            5.0.pow(index.toDouble()).toLong() * number
        }.sum()

}