package y2022.day25

import java.nio.file.Files
import java.nio.file.Paths

object Parser {
    data class Snafu(val digits: List<Int>) {
        override fun toString() = buildString {
            digits.forEach { append(when(it) {
                -1 -> "-"
                -2 -> "="
                else -> it.toString()
            }) }
        }
    }

    fun parse(file: String) = Files.readAllLines(
        Paths.get(
            System.getProperty("user.dir"),
            "src/main/kotlin/y2022/day25/$file"
        )
    ).map { raw ->
        Snafu(raw.map { char ->
            when (char) {
                '-' -> -1
                '=' -> -2
                else -> char.toString().toInt()
            }
        })
    }
}