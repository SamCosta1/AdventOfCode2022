package utils

import kotlin.math.pow

object BinaryUtils {

    fun fromBinary(binary: List<Int>) = binary.reversed().mapIndexed { index, i ->
        i * 2.0.pow(index.toDouble())
    }.sum()
}