package y2023.day23

import utils.GenericGrid

object Parser {
    enum class HikeCell(val raw: Char): GenericGrid.GenericGridItem {
        Trail('.'),
        RightSlope('>'),
        LeftSlope('<'),
        DownSlope('v'),
        Forest('#'),
        Path('O');
        override val char: String
            get() = raw.toString()
    }

    fun parse(data: List<String>) = GenericGrid<HikeCell>(HikeCell.Forest).also { grid ->
        data.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                grid[x, y] = HikeCell.entries.first { it.raw == c }
            }
        }
    }
}