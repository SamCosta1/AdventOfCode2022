package y2023.day15

import puzzlerunners.Puzzle
import utils.RunMode

class Main(
    override val part1ExpectedAnswerForSample: Any = 1320,
    override val part2ExpectedAnswerForSample: Any = 145,
    override val isComplete: Boolean = false
): Puzzle {
    override fun runPart1(data: List<String>, runMode: RunMode): Any = data.sumOf { row ->
        row.split(",").sumOf { subString ->
            subString.hash()
        }
    }

    private fun String.hash(): Int {
        var current = 0
        forEach { char ->
            current = ((current + char.code) * 17) % 256
        }
        return current
    }

    data class LensInBox(val label: String, var focalLength: Int)
    override fun runPart2(data: List<String>, runMode: RunMode) = Parser.parse(data).let { instructions ->
        val boxes = (0 until 256).map { LinkedHashMap<String, LensInBox>() }

        instructions.forEach { instruction ->
            val box = boxes[instruction.label.hash()]
            when (instruction.op) {
                '-' -> {
                    box.remove(instruction.label)
                }
                '=' -> {
                    val existing = box.get(instruction.label)
                    if (existing != null) {
                        existing.focalLength = instruction.focalLength!!
                    } else {
                        box.put(instruction.label, LensInBox(instruction.label, instruction.focalLength!!))
                    }
                }
                else -> throw Exception("Invalid instruction ${instruction.op}")
            }
        }

        boxes.mapIndexed { boxIndex, box ->
            box.values.mapIndexed { index, lensInBox ->
                (boxIndex + 1) * (index + 1) * lensInBox.focalLength
            }.sum()
        }.sum()
    }
}
