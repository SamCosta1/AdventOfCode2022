package y2021.day4

object Parser {

    /**
     * grid[0,0] is top left
     */
    class Board(val name: String, val grid: List<List<Int>>) {
        val drawn = mutableSetOf<Int>()

        var completedColumns = 0
        var completedRows = 0
        fun markDrawn(value: Int) {
            drawn.add(value)
            updateState()
        }

        private fun updateState() {
            if (drawn.size < 4) {
                return
            }

            completedRows = grid.count { row -> row.all { drawn.contains(it) } }
            completedColumns = (0..grid.first().lastIndex).count { x -> grid.all { row -> drawn.contains(row[x]) } }
        }
    }

    data class Data(val drawOrder: List<Int>, val boards: List<Board>)

    fun parse(raw: List<String>): Data {
        val drawList = raw.first().split(",").map { it.toInt() }
        val boards = raw.drop(1).chunked(6).map { it.drop(1) }

        return Data(drawList,
            boards.mapIndexed { index, board ->
                Board("boARD $index", board.map { row ->
                    row.split(" ").filter {
                        it.isNotBlank()
                    }.map { it.toInt() }
                })
            }
        )
    }
}