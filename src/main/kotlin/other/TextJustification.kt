package other


typealias Line = List<String>


fun fullJustify(words: List<String>, maxWidth: Int): List<String> {
    // 1. Split words into lines
    val lines = mutableListOf<Line>()
    lines.add(emptyList())
    // 1.x
    // Loop through the words adding them to a line, once that line is full,
    // move on to the next line

    var currentLineStartIndex = 0 // Index in `words` of the the current line start
    words.forEachIndexed { index, word ->
        val subset = words.subList(currentLineStartIndex, index + 1)

        if (subset.sumOf { it.length } + (subset.size - 1) <= maxWidth) {
            lines[lines.lastIndex] = subset
        } else {
            currentLineStartIndex = index
            lines.add(listOf(word))
        }
    }

    return lines.mapIndexed { index, line ->
        if (index == lines.lastIndex) {
            // 3. Justify nth line (n = #number of lines)
            line.leftJustified(maxWidth)
        } else {
            // 2. Justify first n-1 lines
            line.evenJustified(maxWidth)
        }
    }
}

private fun Line.leftJustified(
    maxWidth: Int
) = joinToString(" ").padEnd(maxWidth)

private fun Line.evenJustified(maxWidth: Int): String {
    if (size == 1) {
        return leftJustified(maxWidth)
    }

    val totalFreeSpace = maxWidth - sumOf { it.length }
    val slots = size - 1
    val spacesPerSlot = totalFreeSpace / slots

    // Number of additional spaces to allocate greedily.
    var remainder = totalFreeSpace % slots

    val line = this@evenJustified
    return buildString {
        line.forEachIndexed { index, word ->
            append(word)

            if (index != line.lastIndex) {
                append(" ".repeat(spacesPerSlot + if (remainder > 0) 1 else 0))
                remainder--
            }
        }
    }
}

object TextJustification {
    fun run() {
        mapOf(
            Pair(listOf("This", "is", "an", "example", "of", "text", "justification."), 16)
                    to listOf(
                "This    is    an",
                "example  of text",
                "justification.  "
            ),
            Pair(listOf("What", "must", "be", "acknowledgment", "shall", "be"), 16) to
                    listOf(
                        "What   must   be",
                        "acknowledgment  ",
                        "shall be        "
                    ),
            Pair(
                listOf(
                    "Science",
                    "is",
                    "what",
                    "we",
                    "understand",
                    "well",
                    "enough",
                    "to",
                    "explain",
                    "to",
                    "a",
                    "computer.",
                    "Art",
                    "is",
                    "everything",
                    "else",
                    "we",
                    "do"
                ), 20
            )
                    to listOf(
                "Science  is  what we",
                "understand      well",
                "enough to explain to",
                "a  computer.  Art is",
                "everything  else  we",
                "do                  "
            )
        ).forEach {
            val fullJustify = fullJustify(it.key.first, it.key.second)
            if (it.value != fullJustify) {
                println("Expected:  |\n" + it.value.joinToString("\n") + "\n|")
                println("Actual  :  |\n" + fullJustify.joinToString("\n") + "\n|")
                println()
            }
        }
    }
}

//
//typealias Line = List<String>
//
//data class WordsRange(val startIndex: Int, val endIndex: Int)
//fun fullJustify(words: List<String>, maxWidth: Int): List<String> {
//    val resultingLines = mutableListOf<Line>()
//    resultingLines.add(emptyList())
//
//    var startIndex = 0
//    var currentSum = 0
//    words.forEachIndexed { index, word ->
//        val subList = words.subList(startIndex, index + 1)
//        currentSum += subList.last().length
//        if (currentSum + (subList.size - 1) <= maxWidth) {
//            resultingLines[resultingLines.lastIndex] = subList
//        } else {
//            startIndex = index
//            currentSum = word.length
//            resultingLines.add(listOf(word))
//        }
//    }
//
//    return resultingLines.dropLast(1).map {
//        evenlyDistribute(it, maxWidth)
//    } + leftDistribute(resultingLines.last(), maxWidth)
//}
//
//private fun evenlyDistribute(words: List<String>, maxWidth: Int): String {
//    if (words.size == 1) {
//        return leftDistribute(words, maxWidth)
//    }
//
//    val slots = words.size - 1
//    val emptySpace = maxWidth - words.sumBy { it.length }
//    val minSpacesPerSlot = emptySpace / slots
//
//    var remainder = emptySpace % slots
//
//    return buildString {
//        words.dropLast(1).forEach {
//            append(it)
//            append(" ".repeat(minSpacesPerSlot + if (remainder <= 0) 0 else 1))
//            remainder--
//        }
//        append(words.last())
//    }
//
//}