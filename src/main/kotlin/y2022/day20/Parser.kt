package y2022.day20

import java.nio.file.Files
import java.nio.file.Paths

data class Entry(val initialPosition: Int, val value: Long)

object Parser {

    fun parse(file: String) = Files.readAllLines(
        Paths.get(
            System.getProperty("user.dir"),
            "src/main/kotlin/y2022/day20/$file"
        )
    ).mapIndexed { index, raw ->
        Entry(index, raw.toLong())
    }
}