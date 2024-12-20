package y2024.day18

import utils.GenericGrid
import utils.Point
import utils.RunMode

object Parser {

    enum class Item : GenericGrid.GenericGridItem {
        Corrupted,
        Free;

        override val char: String
            get() = when (this) {
                Corrupted -> "#"
                Free -> "."
            }
    }

    fun parse(data: List<String>, runMode: RunMode) = GenericGrid<Item>(Item.Corrupted).apply {
        val gridSize = when(runMode) {
            RunMode.Sample -> 6
            RunMode.Real -> 70
        }

        (0..gridSize).forEach {  x->
            (0..gridSize).forEach { y ->
                this[x, y] = Item.Free
            }
        }
        data.take(
            when (runMode) {
                RunMode.Sample -> 12
                RunMode.Real -> 1024
            }
        ).map { row ->
            val (x, y) = row.split(",").map { it.toLong() }
            Point(x, y)
        }.forEach { this[it] = Item.Corrupted }
    }
}