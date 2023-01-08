package day1

import java.nio.file.Files
import java.nio.file.Paths

object Day1Data {
    val sample = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/day1/data.txt"))
}