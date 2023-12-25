package utils

data class Point(val x: Long, val y: Long) {
    constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())

    override fun toString() = "($x,$y)"

    val topLeft get() = Point(x - 1, y - 1)
    val left get() = Point(x - 1L, y)
    val bottomLeft get() = Point(x - 1, y + 1)
    val bottom get() = Point(x, y + 1)
    val bottomRight get() = Point(x + 1, y + 1)
    val right get() = Point(x + 1, y)
    val topRight get() = Point(x + 1, y - 1)
    val top get() = Point(x, y - 1)

    operator fun invoke(direction: MovementDirection) = when(direction) {
        MovementDirection.North -> top
        MovementDirection.South -> bottom
        MovementDirection.West -> left
        MovementDirection.East -> right
    }

    fun adjacentPoints() = listOf(
        Point(x - 1, y - 1),
        Point(x - 1, y),
        Point(x - 1, y + 1),
        Point(x + 1, y + 1),
        Point(x + 1, y),
        Point(x + 1, y - 1),
        Point(x, y + 1),
        Point(x, y - 1)
    )
}