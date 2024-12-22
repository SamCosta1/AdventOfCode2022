package y2024.day19

import puzzlerunners.NotStarted
import puzzlerunners.Puzzle
import utils.RunMode


typealias Combo = Pair<ULong, ULong>

class Main(
    override val part1ExpectedAnswerForSample: Any = 6,
    override val part2ExpectedAnswerForSample: Any = 16L,
    override val isComplete: Boolean = true
) : Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (towels, combos) ->
        val trie = TrieNode(false)
        towels.forEach { trie.insert(it.reversed()) }

        combos.count { combo ->
            numberOfCombos(combo, trie) > 0
        }
    }

    class TrieNode(var isEndOfWord: Boolean, val children: Array<TrieNode?> = arrayOfNulls(26)) {
        fun insert(string: String) {
            var current = this

            string.forEachIndexed { index, char ->
                val childNodeIndex = char - 'a'

                if (current.children[childNodeIndex] == null) {
                    current.children[childNodeIndex] = TrieNode(false)
                }
                current = current.children[childNodeIndex]!!
            }
            current.isEndOfWord = true
        }
    }
    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { (towels, combos) ->
        val trie = TrieNode(false)
        towels.forEach { trie.insert(it.reversed()) }

        combos.sumOf { combo ->
            numberOfCombos(combo, trie)
        }
    }

    private fun numberOfCombos(combo: String, trie: TrieNode): Long {
        val count = Array<Long>(combo.length) { 0 }

        for (i in count.indices) {
            var ptr: TrieNode? = trie
            for (j in i downTo 0) {
                val ch: Char = combo[j]
                val index = ch.code - 'a'.code
                if (ptr?.children?.get(index) == null) {
                    break
                }
                ptr = ptr.children[index]
                if (ptr!!.isEndOfWord) {
                    count[i] = count[i] + if (j > 0) count[j - 1] else 1
                }
            }
        }
        return count[count.lastIndex]
    }
}
