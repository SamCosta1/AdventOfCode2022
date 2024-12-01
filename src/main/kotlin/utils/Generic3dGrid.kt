package utils

import kotlin.math.max
import kotlin.math.min

data class Point3D(val x: Long, val y: Long, val z: Long) {
    constructor(x: Int, y: Int, z: Int) : this(x.toLong(), y.toLong(), z.toLong())

    operator fun minus(other: Point3D) = Point3D(x - other.x, y - other.y, z - other.z)
    operator fun div(other: Point3D) = Point3D(x / other.x, y / other.y, z / other.z)
    operator fun div(d: Long) = Point3D(x / d, y / d, z / d)
    operator fun times(d: Long) = Point3D(x * d, y * d, z * d)
}

class Generic3dGrid<Item : GenericGrid.GenericGridItem>(val defaultItem: Item) {
    val point3Ds = mutableMapOf<Point3D, Item>()
    var topLeftMostPoint3D = Point3D(Long.MAX_VALUE - 3, 0, 0)
    var bottomRightMostPoint3D = Point3D(Long.MIN_VALUE + 3, Long.MIN_VALUE + 3, Long.MIN_VALUE + 3)
    var printBuffer = 0

    operator fun get(point3D: Point3D) = point3Ds.getOrDefault(point3D, defaultItem)
    operator fun get(x: Long, y: Long, z: Long) = point3Ds.getOrDefault(Point3D(x, y, z), defaultItem)
    operator fun set(x: Long, y: Long, z: Long, item: Item) = set(Point3D(x, y, z), item)
    operator fun set(x: Int, y: Int, z: Int, item: Item) = set(Point3D(x.toLong(), y.toLong(), z.toLong()), item)
    operator fun set(point3D: Point3D, item: Item) {
        point3Ds[point3D] = item

        topLeftMostPoint3D = Point3D(
            min(topLeftMostPoint3D.x, point3D.x),
            min(topLeftMostPoint3D.y, point3D.y),
            min(topLeftMostPoint3D.z, point3D.z),
        )
        bottomRightMostPoint3D = Point3D(
            max(bottomRightMostPoint3D.x, point3D.x),
            max(bottomRightMostPoint3D.y, point3D.y),
            max(bottomRightMostPoint3D.z, point3D.z),
        )
    }

    fun copy() = Generic3dGrid(defaultItem).also { newGrid ->
        point3Ds.forEach {
            newGrid[it.key] = it.value
        }
    }

}