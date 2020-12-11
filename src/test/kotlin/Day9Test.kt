import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day9Test {


    @Test
    fun `test real input`() {
        println(
            getFirstInvalidNumber(
                readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day9.txt"),
                25
            )
        )
    }

    @Test
    fun `test real input part 2`() {
        println(
            getSmallestAndLargestInSet2(
                1721308972,
                readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day9.txt").map { it.toLong() })
        )
    }


    @Test
    fun `test demo input`() {
        val input = "35\n" +
                "20\n" +
                "15\n" +
                "25\n" +
                "47\n" +
                "40\n" +
                "62\n" +
                "55\n" +
                "65\n" +
                "95\n" +
                "102\n" +
                "117\n" +
                "150\n" +
                "182\n" +
                "127\n" +
                "219\n" +
                "299\n" +
                "277\n" +
                "309\n" +
                "576"

        assertEquals(127, getFirstInvalidNumber(input.lines(), 5))
    }

    @Test
    fun `test demo input_part2`() {
        val input = "35\n" +
                "20\n" +
                "15\n" +
                "25\n" +
                "47\n" +
                "40\n" +
                "62\n" +
                "55\n" +
                "65\n" +
                "95\n" +
                "102\n" +
                "117\n" +
                "150\n" +
                "182\n" +
                "127\n" +
                "219\n" +
                "299\n" +
                "277\n" +
                "309\n" +
                "576"

        assertEquals(62, getSmallestAndLargestInSet(127, input.lines().map { it.toLong() }))
    }

    private fun getSmallestAndLargestInSet(sumTo: Long, numbers: List<Long>): Long {
        for (i in numbers.indices) {
            var biggerThanSumTo = false
            var counter = 1
            while (!biggerThanSumTo) {
                val sublist = numbers.subList(i, i + counter + 1)
                val summedTo = sublist.sum()
                if (summedTo == sumTo) return findSmallestAndBiggestInRange(sublist)
                if (summedTo > sumTo) biggerThanSumTo = true
                counter++
            }

        }
        throw IllegalArgumentException("Found no set")
    }

    private fun findSmallestAndBiggestInRange(list: List<Long>): Long {
        return list.minOrNull()!! + list.maxOrNull()!!
    }


    @Test
    fun `test is valid number`() {
        assertTrue(isValidNumber(40, listOf(35, 20, 15, 25, 47)))
        assertTrue(isValidNumber(62, listOf(20, 15, 25, 47, 40)))
        assertTrue(isValidNumber(55, listOf(15, 25, 47, 40, 62)))
        assertFalse(isValidNumber(127, listOf(182, 150, 117, 102, 95)))
    }

    fun isValidNumber(number: Long, preamble: List<Long>): Boolean {
        return preamble.any { (preamble - listOf(it)).toSet().contains(number - it) }
    }

    fun getFirstInvalidNumber(input: List<String>, preambleLength: Int): Long {
        val numbers = input.map { it.toLong() }
        for (i in (preambleLength..numbers.size)) {
            val numberToCheck = numbers[i]
            if (!isValidNumber(numberToCheck, numbers.subList(i - preambleLength, i))) {
                return numberToCheck
            }
        }

        throw IllegalArgumentException("All numbers were valid")
    }


    private fun getSmallestAndLargestInSet2(sumTo: Long, numbers: List<Long>): Long {
        for (i in numbers.indices) {
            for (j in i + 1 until numbers.size) {
                if (numbers.subList(i, j).sum() == sumTo) return findSmallestAndBiggestInRange(numbers.subList(i, j))
            }
        }
        throw IllegalArgumentException("Found no set")
    }

}