import Day18.Operator.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals

class Day18 {


    @Test
    fun `test demo input`() {
        println(evaluateExpressionAdditionPrecidence("((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2"))
    }

    enum class Operator {
        Addition, Multiplication;
    }

    @Test
    fun `real data`() {
        println(readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day18.txt").map {
            evaluateExpressionAdditionPrecidence(
                it
            )
        }.sumOf { it.first })
    }

    @Test
    fun `test operator`() {
        assertEquals(21, operate(Multiplication, 7, 3))
        assertEquals(10, operate(Addition, 7, 3))
    }

    private fun operate(operator: Operator?, paramOne: Long, paramTwo: Long): Long {
        return when (operator) {
            Addition -> paramOne + paramTwo
            Multiplication -> paramOne * paramTwo
            else -> throw IllegalArgumentException("No operator!")
        }
    }

    private fun evaluateExpressionLeftToRight(expressionString: String): Pair<Long, Long> {
        val spaceRemovedExpressionString = expressionString.replace(" ", "")
        return Pair(
            spaceRemovedExpressionString.foldIndexed(
                Triple<Long, Operator?, Long>(
                    0,
                    null,
                    0
                )
            ) { index, (acc, operator, skips), c ->
                if (skips > 0L) {
                    Triple(acc, operator, skips - 1)
                } else {
                    when (c) {
                        '+' -> Triple(acc, Addition, 0L)
                        '*' -> Triple(acc, Multiplication, 0L)
                        '(' -> {
                            val (valueOfExpressionInParanthesis, skips) = evaluateExpressionLeftToRight(
                                spaceRemovedExpressionString.substring(
                                    index + 1
                                )
                            )
                            if (operator == null) Triple(valueOfExpressionInParanthesis, null, skips) else Triple(
                                operate(
                                    operator,
                                    acc,
                                    valueOfExpressionInParanthesis
                                ), null, skips
                            )
                        }
                        ')' -> return Pair(acc, index + 1L)
                        else -> {
                            //Handle that the numbers might now be more than one digits since I reuse this one for part two multiplication when I handled the addition
                            val restOfString = spaceRemovedExpressionString.substring(index)
                            val newNumber = restOfString.substringBefore("*").substringBefore("+")
                            val newNumberLong = newNumber.toLong()
                            if (operator == null) Triple(newNumberLong, null, newNumber.length - 1L) else Triple(
                                operate(
                                    operator,
                                    acc,
                                    newNumberLong
                                ), null, newNumber.length - 1L
                            )
                        }
                    }
                }

            }.first, 0L
        )
    }


    private fun evaluateExpressionAdditionPrecidence(expressionString: String): Pair<Long, Long> {
        val spaceRemovedExpressionString = expressionString.replace(" ", "")
        // First only do the additions
        val firstPass =
            spaceRemovedExpressionString.foldIndexed(
                Quadruple<Long, Operator?, Long, String>(
                    0,
                    null,
                    0,
                    ""
                )
            ) { index, (acc, operator, skips, stringForSecondPass), c ->
                if (skips != 0L) {
                    Quadruple(acc, operator, skips - 1, stringForSecondPass)
                } else {
                    when (c) {
                        '+' -> Quadruple(acc, Addition, 0L, stringForSecondPass)
                        // Multiplication we handle on the secondpass, so build up the string for that, which is what we have before, what we have added an the multiplication sign
                        '*' -> Quadruple(0, null, 0L, stringForSecondPass + acc.toString() + "*")
                        '(' -> {
                            val (valueOfExpressionInParanthesis, skips) = evaluateExpressionAdditionPrecidence(
                                spaceRemovedExpressionString.substring(
                                    index + 1
                                )
                            )
                            if (operator == null) Quadruple(
                                valueOfExpressionInParanthesis,
                                null,
                                skips,
                                stringForSecondPass
                            ) else Quadruple(
                                operate(
                                    operator,
                                    acc,
                                    valueOfExpressionInParanthesis
                                ), null, skips, stringForSecondPass
                            )
                        }
                        ')' -> return Pair(
                            evaluateExpressionLeftToRight(stringForSecondPass + acc.toString()).first,
                            index + 1L
                        )
                        else -> if (operator == null) Quadruple(
                            c.toString().toLong(),
                            null,
                            0,
                            stringForSecondPass
                        ) else Quadruple(
                            operate(
                                operator,
                                acc,
                                c.toString().toLong()
                            ), null, 0, stringForSecondPass
                        )
                    }
                }

            }
        val stringForSecondPas = firstPass.fourth + firstPass.first.toString()
        val afterSecondPass = evaluateExpressionLeftToRight(stringForSecondPas)

        return Pair(afterSecondPass.first, 0L)
    }


}
