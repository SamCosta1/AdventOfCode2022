package y2022.day20

import java.util.*

class Day20Main(file: String) {

    val data = Parser.parse(file)

    fun run(): Long {
        val indiciesOfInterest = listOf(1000, 2000, 3000)

        val sequence = LinkedList(data)
        data.forEach { entry ->
            if (entry.value != 0L) {
                val currentIndex = sequence.indexOf(entry)
                val newPos = (currentIndex + entry.value).let { Math.floorMod(it, data.size - 1L) }

                sequence.remove(entry)
                sequence.add(newPos.toInt(), entry)
            }
        }

        val zeroIndex = sequence.indexOfFirst { it.value == 0L }
        return indiciesOfInterest.sumOf {
            sequence[(zeroIndex + it) % sequence.size].value
        }
    }

    fun runPart2(): Long {
        val indiciesOfInterest = listOf(1000, 2000, 3000)
        val newData = data.map { it.copy(value = it.value * 811589153L) }

        val sequence = LinkedList(newData)

        repeat(10) {
            newData.forEach { entry ->
                if (entry.value != 0L) {
                    val currentIndex = sequence.indexOf(entry)
                    val newPos = (currentIndex + entry.value).let { Math.floorMod(it, newData.size - 1L) }

                    sequence.remove(entry)
                    sequence.add(newPos.toInt(), entry)
                }
            }
        }

        val zeroIndex = sequence.indexOfFirst { it.value == 0L }
        return indiciesOfInterest.sumOf {
            sequence[(zeroIndex + it) % sequence.size].value
        }
    }
}

fun Collection<Entry>.values() = map { it.value }.joinToString()

//wrong 8401
//Initial arrangement:
//1, 2, -3, 3, -2, 0, 4
//
//1 moves between 2 and -3:
//2, 1, -3, 3, -2, 0, 4
//
//2 moves between -3 and 3:
//1, -3, 2, 3, -2, 0, 4
//
//-3 moves between -2 and 0:
//1, 2, 3, -2, -3, 0, 4
//
//3 moves between 0 and 4:
//1, 2, -2, -3, 0, 3, 4
//
//-2 moves between 4 and 1:
//1, 2, -3, 0, 3, 4, -2
//
//0 does not move:
//1, 2, -3, 0, 3, 4, -2
//
//4 moves between -3 and 0:
//1, 2, -3, 4, 0, 3, -2


// 9, 2, -3, 3, -2, -1, 4   ,9, 2, -3, 3, -2, -1, 4,    9, 2, -3, 3, -2, -1, 4

//9: 0 -> 2
//14: 0 - > 0

//1, -3, 2, 3, -2, 0, 4, 1, -3, 2, 3, -2, 0, 4