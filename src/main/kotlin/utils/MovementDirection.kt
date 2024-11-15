package utils

enum class MovementDirection {
    North,
    South,
    West,
    East;

    val normals get() = when(this) {
        North,
        South -> listOf(East, West)
        West,
        East -> listOf(North, South)
    }

    val char get() = when(this) {
        North -> '^'
        South -> 'v'
        West -> '<'
        East -> '>'
    }
}