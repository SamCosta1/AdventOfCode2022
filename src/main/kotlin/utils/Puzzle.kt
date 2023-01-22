package utils

interface Puzzle {
    fun runPart1(data: List<String>, runMode: RunMode): Any
    fun runPart2(data: List<String>, runMode: RunMode): Any
}