package utils

import java.math.BigDecimal

fun gaussianElimination(coefficients: List<MutableList<Double>>): List<Double> {
    val rows = coefficients.size
    val cols = coefficients.first().size

    // This only works on a square matrix (with one extra column for the
    // coefficient on the right-hand side of the equation).
    require(rows == cols - 1) {
        throw Exception(
            "The number of coefficients on the left side of the" +
                    "equation should be equal to the number of equations."
        )
    }

    // We operate on each row in the matrix of coefficients.
    for (row in coefficients.indices) {

        // Normalize the row starting with the diagonal value of each row.
        val pivot = coefficients[row][row]
        for (col in coefficients[row].indices) {
            coefficients[row][col] /= pivot
        }

        // Sweep the other rows with `row`
        for (otherRow in coefficients.indices) {
            if (row == otherRow) continue

            val factor = coefficients[otherRow][row]
            for (col in coefficients[otherRow].indices) {
                coefficients[otherRow][col] -= factor * coefficients[row][col]
            }
        }
    }

    return coefficients.map { it.last() }.toList()
}