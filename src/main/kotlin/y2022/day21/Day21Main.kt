package y2022.day21

class Day21Main(file: String) {

    private val data = Parser.parse(file)
    private val root = data.first { it.name == "root" }

    fun run(): Long = computeValue(root).value

    fun runPart2(): Long {
        val targetMonkey = data.first { it.name == "humn" }

        val operand1 = (root.action as Action.PerformOperation).val1
        val operand2 = (root.action as Action.PerformOperation).val2
        val op1Res = kotlin.runCatching { computeValue(operand1, targetMonkey) }.getOrNull()
        val op2Res = kotlin.runCatching { computeValue(operand2, targetMonkey) }.getOrNull()

        return when {
            op1Res != null -> backwardCompute(operand2, op1Res.value, targetMonkey)
            op2Res != null -> backwardCompute(operand1, op2Res.value, targetMonkey)
            else -> throw Exception("This should never happen")

        }
    }

    private fun backwardCompute(monkey: Monkey, target: Long, targetMonkey: Monkey): Long {
        if (monkey == targetMonkey) {
            return target
        }

        val action = (monkey.action as? Action.PerformOperation)
            ?: throw Exception("Value monkeys should never be backward computed")

        val operand1 = action.val1
        val operand2 = action.val2
        val op1Res = kotlin.runCatching { computeValue(operand1, targetMonkey) }.getOrNull()?.value
        val op2Res = kotlin.runCatching { computeValue(operand2, targetMonkey) }.getOrNull()?.value

        val nextMonkey = if (op1Res == null) operand1 else operand2
        if (op1Res == null && op2Res == null) {
            throw Exception("One should be non null")

        }
        val newTarget = when (action.op) {
            Action.Operation.Sum -> target - (op1Res ?: op2Res)!!
            Action.Operation.Subtract -> if (op2Res != null) {
                target + op2Res
            } else {
                op1Res!! - target
            }
            Action.Operation.Multiply -> target / (op1Res ?: op2Res)!!
            Action.Operation.Divide -> if (op2Res != null) {
                target * op2Res
            } else {
                op1Res!! / target
            }
        }

        return backwardCompute(nextMonkey, newTarget, targetMonkey)
    }

    private fun computeValue(
        monkey: Monkey,
        invalidMonkey: Monkey? = null
    ): Action.Value = if (monkey == invalidMonkey) throw Exception("Bit of a hack but meh") else
        when (val action = monkey.action) {
            is Action.Value -> {
                action
            }
            is Action.PerformOperation -> {
                val operand1Value = computeValue(action.val1, invalidMonkey)
                val operand2Value = computeValue(action.val2, invalidMonkey)
                val thisValue = action.op.performOp(operand1Value, operand2Value)

                thisValue
            }
        }

    private fun Action.Operation.performOp(
        operand1: Action.Value,
        operand2: Action.Value
    ): Action.Value = when (this) {
        Action.Operation.Sum -> operand1.value + operand2.value
        Action.Operation.Subtract -> operand1.value - operand2.value
        Action.Operation.Multiply -> operand1.value * operand2.value
        Action.Operation.Divide -> operand1.value / operand2.value
    }.let { Action.Value(it) }
}