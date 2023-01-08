package day7

import java.nio.file.Files
import java.nio.file.Paths

object Day7Main {
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

    val data =
        Files.readAllLines(Paths.get(System.getProperty("user.dir"), "src/main/kotlin/day7/data.txt")).let { rawList ->
            parseDir(rawList)
        }

    private fun parseDir(rawLines: List<String>): Entry.Dir {

        val root = Entry.Dir("/", 0, mutableListOf(), null)

        var currentNode = root

        rawLines.drop(1).map { it.parseLine() }.forEach { info ->
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

    fun Int.debug() = String.format("%02d", this)
    fun Long.debug() = String.format("%02d", this)

    private fun findEntriesMatching(root: Entry.Dir, block: (Entry) -> Boolean): List<Entry> {
        val matchingChildren = root.entries.filter { block(it) }
        val matchingDescendants = root.entries.mapNotNull { it as? Entry.Dir }.map { findEntriesMatching(it, block) }.flatten()
        return matchingDescendants + matchingChildren
    }

    fun run() = findEntriesMatching(data) { it is Entry.Dir }.filter { it.size <= 100000 }.sumBy { it.size.toInt() }

    fun runPart2() : String {

        val diskSize = 70000000
        val unusedSpaceTarget =  30000000

        val currentFreeSpace = diskSize - data.size
        val targetDirSize = unusedSpaceTarget - currentFreeSpace
        println("\t\t Root Size: ${data.size}")
        println("\t\t Target Dir Size: $targetDirSize")

        return findEntriesMatching(data) { it is Entry.Dir }.filter { it.size > targetDirSize }.minOf { it.size }.toString()
    }
}
