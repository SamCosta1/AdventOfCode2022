import utils.MovementDirection

data class PointInt(val x: Int, val y: Int) {

    override fun toString() = "($x,$y)"

    val topLeft get() = PointInt(x - 1, y - 1)
    val left get() = PointInt(x - 1, y)
    val bottomLeft get() = PointInt(x - 1, y + 1)
    val bottom get() = PointInt(x, y + 1)
    val bottomRight get() = PointInt(x + 1, y + 1)
    val right get() = PointInt(x + 1, y)
    val topRight get() = PointInt(x + 1, y - 1)
    val top get() = PointInt(x, y - 1)

    operator fun invoke(direction: MovementDirection) = when (direction) {
        MovementDirection.North -> top
        MovementDirection.South -> bottom
        MovementDirection.West -> left
        MovementDirection.East -> right
    }

    operator fun invoke(direction: MovementDirection, amount: Int) = when (direction) {
        MovementDirection.North -> PointInt(x, y - amount)
        MovementDirection.South -> PointInt(x, y + amount)
        MovementDirection.West -> PointInt(x - amount, y)
        MovementDirection.East -> PointInt(x + amount, y)
    }

    fun adjacentPointInts() = listOf(
        PointInt(x - 1, y - 1),
        PointInt(x - 1, y),
        PointInt(x - 1, y + 1),
        PointInt(x + 1, y + 1),
        PointInt(x + 1, y),
        PointInt(x + 1, y - 1),
        PointInt(x, y + 1),
        PointInt(x, y - 1)
    )

    fun adjacentNoDiagonal() = listOf(top, left, right, bottom)

    operator fun minus(other: PointInt) = PointInt(x - other.x, y - other.y)
    operator fun plus(other: PointInt) = PointInt(x + other.x, y + other.y)
}
