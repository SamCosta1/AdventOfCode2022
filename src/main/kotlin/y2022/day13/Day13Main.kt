package y2022.day13

import y2022.day13.Parsing.parse
import java.nio.file.Files
import java.nio.file.Paths

class Day13Main {

    data class Pair(val left: Item.MyList, val right: Item.MyList)
    sealed class Item {
        abstract fun toMyList(): MyList
        data class MyList(val list: List<Item>) : Item() {
            override fun toString(): String {
                return list.toString().filter { !it.isWhitespace() }
            }

            override fun toMyList() = this
        }

        data class Integer(val integer: Int) : Item() {
            override fun toString(): String {
                return integer.toString()
            }

            override fun toMyList() = MyList(listOf(this))
        }

    }

    val data = Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/y2022/day13/data.txt"))
        .parse()

    fun run(): Int {
        var indicies = mutableListOf<Int>()
        data.forEachIndexed { index, pair ->
            val rightOrder = isRightOrder(pair.left, pair.right) ?: throw Exception("Right order was null")
            if (rightOrder) {
                indicies.add(index + 1)
            }
        }
        return indicies.sum()
    }

    private fun isRightOrder(left: Item, right: Item): Boolean? {
//        println("Compare $left vs $right")
        if (left is Item.Integer && right is Item.Integer) {
            return if (left == right) {
                null // Cannot be determined, move on to next cycle
            } else {
                return if (left.integer < right.integer) {
//                    println("Left side smaller - right order")
                    true
                } else {
//                    println("Right side smaller - wrong order")
                    false
                }
            }
        }

        if (left is Item.MyList && right is Item.MyList) {
            left.list.forEachIndexed { index, leftItem ->
                val rightItem = right.list.getOrNull(index) ?: run {
//                   println("Right ran out - wrong order")
                    return false // right ran out
                }
                val result = isRightOrder(leftItem, rightItem)

                if (result == null) {

                } else {
                    return result
                }
            }

            val leftRanOutStrictlyFaster = left.list.size < right.list.size

            return if (leftRanOutStrictlyFaster) {
                //                println("Left ran out - correct order $left $right")
                true
            } else {
                null
            }

        }

//        println("Mixed types: Rerunning ${left.toMyList()} and ${right.toMyList()}")
        return isRightOrder(left.toMyList(), right.toMyList())
    }


    val divider1 = Item.MyList(listOf(Item.MyList(listOf(Item.Integer(2)))))
    val divider2 = Item.MyList(listOf(Item.MyList(listOf(Item.Integer(6)))))

    fun runPart2() = data.toMutableList().apply {
        add(
            Pair(
                divider1, divider2
            )
        )
    }.map { listOf(it.left, it.right) }.flatten().sortedWith { o1, o2 ->
        when {
            o1 == o2 -> 0
            isRightOrder(o1, o2) ?: throw Exception("Result null") -> -1
            else -> 1

        }
    }.let {
        val divider1Index = it.indexOf(divider1) + 1
        val divider2Index = it.indexOf(divider2) + 1
        divider1Index * divider2Index
    }

    private fun printRawInput() {
        data.forEach {
            println(it.left)
            println(it.right)
            println("Is Right Order ${isRightOrder(it.left, it.right)}")
            println()
        }
    }
}
