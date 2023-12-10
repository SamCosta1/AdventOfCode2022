package y2022.day7

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 95437,
    override val part2ExpectedAnswerForSample: Any = 24933642,
    override val isComplete: Boolean = false
): Puzzle {
    sealed class RawLineInfo {
        object Ls : RawLineInfo()
        data class Cd(val destination: String) : RawLineInfo()
        data class File(val size: Long, val name: String) : RawLineInfo()
        data class Dir(val name: String) : RawLineInfo()
    }

    sealed class Entry {
        abstract val name: String
        abstract val size: Long

        data class Dir(
            override val name: String,
            override var size: Long,
            val entries: MutableList<Entry>,
            val parent: Entry.Dir?
        ) : Entry()

        data class File(override val name: String, override var size: Long, val parent: Entry) : Entry()

    }

    private fun parse(data: List<String>): Entry.Dir {
        val root = Entry.Dir("/", 0, mutableListOf(), null)

        var currentNode = root

        data.drop(1).map { it.parseLine() }.forEach { info ->
            when (info) {
                RawLineInfo.Ls -> { /* No Op */
                }
                RawLineInfo.Cd("..") -> {
                    currentNode = currentNode.parent ?: throw Exception("Can't go there")
                }
                is RawLineInfo.Dir -> currentNode.entries.add(
                    Entry.Dir(
                        info.name,
                        0,
                        mutableListOf(),
                        parent = currentNode
                    )
                )
                is RawLineInfo.File -> currentNode.entries.add(Entry.File(info.name, info.size, parent = currentNode))
                is RawLineInfo.Cd -> currentNode =
                    (currentNode.entries + currentNode).find { it.name == info.destination } as? Entry.Dir
                        ?: throw Exception("Could not find dir ${info.destination}")
            }
        }

        root.size = computeSize(root)
        return root
    }

    private fun computeSize(dir: Entry.Dir): Long = dir.entries.map { entry ->
        when (entry) {
            is Entry.Dir -> computeSize(entry).also {
                entry.size = it
            }
            is Entry.File -> entry.size
        }
    }.sum()


    private fun String.parseLine(): RawLineInfo = when {
        startsWith("$ ls") -> RawLineInfo.Ls
        startsWith("$ cd") -> RawLineInfo.Cd(split(" ").last())
        startsWith("dir") -> RawLineInfo.Dir(split(" ").last())
        else -> {
            val match =
                "(\\d+) (.*)".toRegex().matchEntire(this)?.groupValues?.drop(1) ?: throw Exception("Can't parse $this")
            RawLineInfo.File(match.first().toLong(), match.last())
        }
    }

    private fun findEntriesMatching(root: Entry.Dir, block: (Entry) -> Boolean): List<Entry> {
        val matchingChildren = root.entries.filter { block(it) }
        val matchingDescendants = root.entries.mapNotNull { it as? Entry.Dir }.map { findEntriesMatching(it, block) }.flatten()
        return matchingDescendants + matchingChildren
    }

    override fun runPart1(
        data: List<String>,
        runMode: RunMode
    ) = findEntriesMatching(parse(data)) { it is Entry.Dir }.filter { it.size <= 100000 }.sumBy { it.size.toInt() }

    override fun runPart2(data: List<String>, runMode: RunMode): Any {
        val diskSize = 70000000
        val unusedSpaceTarget =  30000000

        val currentFreeSpace = diskSize - data.size
        val targetDirSize = unusedSpaceTarget - currentFreeSpace

        return findEntriesMatching(parse(data)) { it is Entry.Dir }.filter { it.size > targetDirSize }.minOf { it.size }.toString()
    }
}
