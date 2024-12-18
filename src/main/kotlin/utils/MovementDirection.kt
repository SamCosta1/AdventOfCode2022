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

    val turnRight90Degrees get() = when(this) {
        North -> East
        South -> West
        West -> North
        East -> South
    }

    val turnLeft90Degrees get() = when(this) {
        North -> West
        South -> East
        West -> South
        East -> North
    }
}