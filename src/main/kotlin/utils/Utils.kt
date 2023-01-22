package utils

import kotlin.system.measureTimeMillis


internal fun runTimed(repeats: Int = 1, block: () -> Any): String {
    var answer: Any? = null
    val time = measureTimeMillis {
        repeat(repeats) {
            answer = block()
        }
    }

    return "$answer | Average time ${time / repeats}ms"
}
