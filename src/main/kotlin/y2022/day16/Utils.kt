package y2022.day16


typealias Permutation = List<Room>

object Utils {

    private fun <T> getSubsets(
        superSet: List<T>,
        k: Int,
        idx: Int,
        current: MutableSet<T>,
        solution: MutableList<Set<T>>
    ) {
        //successful stop clause
        if (current.size == k) {
            solution.add(HashSet(current))
            return
        }
        //unseccessful stop clause
        if (idx == superSet.size) return
        val x = superSet[idx]
        current.add(x)
        //"guess" x is in the subset
        getSubsets(superSet, k, idx + 1, current, solution)
        current.remove(x)
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx + 1, current, solution)
    }

    fun <T> getSubsets(superSet: List<T>, k: Int): List<Set<T>> {
        val res: MutableList<Set<T>> = ArrayList()
        getSubsets(superSet, k, 0, HashSet(), res)
        return res
    }

    var i = 0
    private fun permute(solutions: MutableList<Permutation>, rooms: MutableList<Room>, currentIndex: Int) {

        for (i in currentIndex until rooms.size) {
            val r = rooms[i]
            rooms[i] = rooms[currentIndex]
            rooms[currentIndex] = r

            println("Fixing ${rooms.subList(0, currentIndex).map { it.name }}, running the rest")
            permute(solutions, rooms, currentIndex + 1)

            val r1 = rooms[currentIndex]
            rooms[currentIndex] = rooms[i]
            rooms[i] = r1
        }
        if (currentIndex == rooms.size - 1) {
            println("Finished sol ${rooms.map { it.name }}")
            solutions.add(rooms.map { it })
        }
    }

    fun computeAllPermutations(rooms: List<Room>): List<Permutation> = mutableListOf<Permutation>().also { solutions ->
        permute(solutions = solutions, rooms = rooms.toMutableList(), 0)
    }
}