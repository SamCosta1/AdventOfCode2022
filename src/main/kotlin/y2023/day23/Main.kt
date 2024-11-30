package y2023.day23

import puzzlerunners.Puzzle
import utils.GenericGrid
import utils.Point
import utils.RunMode
import y2022.day17.Rock

class Main(
    override val part1ExpectedAnswerForSample: Any = 94,
    override val part2ExpectedAnswerForSample: Any = 154,
    override val isComplete: Boolean = false,
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val endPoint = Point(grid.bottomRightMostPoint.x - 1, grid.bottomRightMostPoint.y)
        val startCell = Point(1, 0)
        search(startCell, endPoint, mutableSetOf(), grid.copy()) { current ->
            listOfNotNull(
                current.left.takeIf { point -> grid[point].let { it == Parser.HikeCell.Trail || it == Parser.HikeCell.LeftSlope } },
                current.right.takeIf { point -> grid[point].let { it == Parser.HikeCell.Trail || it == Parser.HikeCell.RightSlope } },
                current.top.takeIf { point -> grid[point] == Parser.HikeCell.Trail },
                current.bottom.takeIf { point -> grid[point].let { it == Parser.HikeCell.Trail || it == Parser.HikeCell.DownSlope } }
            )
        }
    }

    var maxSoFar = 0

    fun search(
        _current: Point,
        endPoint: Point,
        _soFar: Set<Point>,
        grid: GenericGrid<Parser.HikeCell>,
        nextPointDecision: (Point) -> List<Point>
    ): Int {

        var current = _current
        val soFar = _soFar.toMutableSet()
        soFar.add(_current)
        var possibleNextPoints = nextPointDecision(current).filter {
            !soFar.contains(it)
        }

        while (possibleNextPoints.size <= 1) {
            if (possibleNextPoints.isEmpty()) {
                return 0
            }

            current = possibleNextPoints.first()
            soFar.add(current)
            if (current == endPoint) {
                if (maxSoFar < soFar.size - 1) {
                    maxSoFar = soFar.size - 1
                }
                return soFar.size - 1
            }

            possibleNextPoints = nextPointDecision(current).filter {
                !soFar.contains(it)
            }
        }

        return possibleNextPoints.maxOfOrNull { p ->
//            soFar.add(current)
//            soFar.add(p)
            search(p, endPoint, soFar.toSet() + current, grid, nextPointDecision).also {
//                soFar.remove(current)
//                soFar.remove(p)
            }
        } ?: 0
    }

    data class Node(val location: Point, var edges: List<Edge>) {
        val connected by lazy { edges.map { listOf(it.n1!!, it.n2!!) }.flatten() }

        data class Edge(val points: MutableList<Point>, var n1: Node?, var n2: Node?) {
            override fun toString(): String {
                return "N1: ${n1?.location} N2: ${n2?.location} "
            }
        }

        override fun equals(other: Any?): Boolean {
            return other is Node && other.location == location
        }

        override fun hashCode(): Int {
            return location.hashCode()
        }
    }

    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { grid ->
        val endPoint = Point(grid.bottomRightMostPoint.x - 1, grid.bottomRightMostPoint.y)
        val startCell = Point(1, 0)

        val startNode = Node(startCell, emptyList())
        val endNode = Node(endPoint, emptyList())
        val alreadyConsidered = mutableMapOf<Point, Node.Edge>()
        val intersections = grid.points.filter { (point, cell) ->
            cell != Parser.HikeCell.Forest && listOf(
                point.top,
                point.left,
                point.bottom,
                point.right
            ).count { grid[it] != Parser.HikeCell.Forest } > 2
        }

        val nodes = intersections.map { Node(it.key, mutableListOf()) }
        intersections.forEach { (intersection, _) ->
            val edgeStartPoints = intersection.connected().filter {
                grid[it] != Parser.HikeCell.Forest
            }

            val thisNode = nodes.first { it.location == intersection }

            thisNode.edges = edgeStartPoints.map { edgeStartPoint ->
                alreadyConsidered.getOrPut(edgeStartPoint) {
                    val edge = Node.Edge(mutableListOf(), thisNode, null)
                    var previous: Point? = null
                    var current = edgeStartPoint

                    while (true) {
                        edge.points.add(current)
                        alreadyConsidered[current] = edge
                        val _current = current.connected().filter {
                            it != previous && grid[it] != Parser.HikeCell.Forest && !intersections.contains(it)
                        }.also {
                            if (it.size > 1) {
                                throw Exception("aaaa")
                            }
                        }.firstOrNull()

                        if (_current == null) { // Reached another intersection
                            break
                        }
                        previous = current
                        current = _current
                    }

                    edge
                }.also { edge ->
                    if (edge.n1 != thisNode) {
                        edge.n2 = thisNode
                    }
                }
            }
        }

        alreadyConsidered.values.first { it.n2 == null }.apply {
            n2 = startNode
            startNode.edges = listOf(this)
        }
        alreadyConsidered.values.last { it.n2 == null }.apply {
            n2 = endNode
            endNode.edges = listOf(this)
        }

        search(startNode, setOf(startNode), endNode)

        routes.map { it.toList() }.maxOf { path ->
            var total = path.size - 3

            for ((index, node) in path.withIndex()) {
                if (index == path.size - 1) {
                    break
                }

                val edge = node.edges.first { it.n1 == path[index + 1] || it.n2 == path[index + 1] }
                total += edge.points.toSet().size
            }

            total
        }
    }

    val routes = mutableListOf<Set<Node>>()
    fun search(fromNode: Node, soFar: Set<Node>, endNode: Node): Int {
        if (fromNode.location == endNode.location) {
            if (endNode.edges.size != 1) {
                throw Exception("oops")
            }
            routes.add(soFar)
            return 0
        }

        return fromNode.edges.maxOf { edge ->
            val otherNode = (if (edge.n1 == fromNode) edge.n2 else edge.n1)!!
            if (!soFar.contains(otherNode)) {
                search(otherNode, soFar + otherNode, endNode)
            } else {
                0
            } + edge.points.size
        }
    }

    fun Point.connected() = listOf(
        top,
        left,
        bottom,
        right
    )

}
