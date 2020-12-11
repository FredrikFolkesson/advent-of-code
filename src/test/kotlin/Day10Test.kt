import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day10Test {


    @Test
    fun `test real input`() {
        println(getNumberOfOneAndThreeVoltageDifferences(readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day10.txt")))
    }

    @Test
    fun `test getAdapterListInOrder`() {
        val input = "16\n" +
                "10\n" +
                "15\n" +
                "5\n" +
                "1\n" +
                "11\n" +
                "7\n" +
                "19\n" +
                "6\n" +
                "12\n" +
                "4"
        assertEquals(listOf(1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19), getAdapterListInOrder(input))
    }

    @Test
    fun `test first demo input`() {
        val input = "16\n" +
                "10\n" +
                "15\n" +
                "5\n" +
                "1\n" +
                "11\n" +
                "7\n" +
                "19\n" +
                "6\n" +
                "12\n" +
                "4"
        assertEquals(35, getNumberOfOneAndThreeVoltageDifferences(input))
    }

    @Test
    fun `test second demo input`() {
        val input = "28\n" +
                "33\n" +
                "18\n" +
                "42\n" +
                "31\n" +
                "14\n" +
                "46\n" +
                "20\n" +
                "48\n" +
                "47\n" +
                "24\n" +
                "23\n" +
                "49\n" +
                "45\n" +
                "19\n" +
                "38\n" +
                "39\n" +
                "11\n" +
                "1\n" +
                "32\n" +
                "25\n" +
                "35\n" +
                "8\n" +
                "17\n" +
                "7\n" +
                "9\n" +
                "4\n" +
                "2\n" +
                "34\n" +
                "10\n" +
                "3"
        assertEquals(220, getNumberOfOneAndThreeVoltageDifferences(input))
    }

    private fun getNumberOfOneAndThreeVoltageDifferences(input: String): Int {
        val adaptersWithoutZeroAndMax = getAdapterListInOrder(input)
        val adapters = listOf(0) + getAdapterListInOrder(input) + listOf(adaptersWithoutZeroAndMax.maxOrNull()!! + 3)
        val differenceList = adapters.windowed(2).map { it.maxOrNull()!! - it.minOrNull()!! }
        val oneDifferences = differenceList.count { it == 1 }
        val threeDifferences = differenceList.count { it == 3 }
        return oneDifferences * threeDifferences


    }

    private fun getAdapterListInOrder(input: String): List<Int> {
        return input.lines().map { it.toInt() }.sorted()
    }

    @Test
    fun `test is valid connections`() {
        val input1 = "16\n" +
                "10\n" +
                "15\n" +
                "5\n" +
                "1\n" +
                "11\n" +
                "7\n" +
                "19\n" +
                "6\n" +
                "12\n" +
                "4"
        assertTrue(isValidList(getAdapterListInOrder(input1)))


        assertTrue(isValidList(listOf(0, 1, 4, 5, 6, 7, 10, 11, 12, 15, 16, 19, 22)))
        assertTrue(isValidList(listOf(0, 1, 4, 5, 6, 7, 10, 12, 15, 16, 19, 22)))
        assertTrue(isValidList(listOf(0, 1, 4, 5, 7, 10, 11, 12, 15, 16, 19, 22)))
        assertTrue(isValidList(listOf(0, 1, 4, 5, 7, 10, 12, 15, 16, 19, 22)))
        assertTrue(isValidList(listOf(0, 1, 4, 6, 7, 10, 11, 12, 15, 16, 19, 22)))
        assertTrue(isValidList(listOf(0, 1, 4, 6, 7, 10, 12, 15, 16, 19, 22)))
        assertTrue(isValidList(listOf(0, 1, 4, 7, 10, 11, 12, 15, 16, 19, 22)))
        assertTrue(isValidList(listOf(0, 1, 4, 7, 10, 12, 15, 16, 19, 22)))


    }

    private fun isValidList(adapters: List<Int>): Boolean {
        return !adapters.windowed(2).filter { !(it.maxOrNull()!! - it.minOrNull()!! <= 3) }.any()
    }

    @Test
    fun `second part`() {
        part2(readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day10.txt"))
    }

    // This is a horrible solution
    private fun part2(input: String) {
        val list = getAdapterListInOrder(input)
        var pairsThatAreNeeded = list.windowed(2).filter { (it.maxOrNull()!! - it.minOrNull()!! == 3) }
        pairsThatAreNeeded = (listOf(listOf(0, 0)) + pairsThatAreNeeded).toMutableList()


        val neededListOfSubLists = mutableListOf<List<Int>>()
        var neededCurrentList = mutableListOf<Int>()
        var oldval = 0
        for (i in pairsThatAreNeeded.flatten().toSet()) {
            if (i - oldval > 3 || (oldval + 1..i - 1).filter { it in list }.any()) {
                neededListOfSubLists.add(neededCurrentList)
                neededCurrentList = mutableListOf()
            }
            neededCurrentList.add(i)

            oldval = i
        }


        val neededNumbers = neededListOfSubLists.flatten().toSet()

        val listOfSubLists = mutableListOf<List<Int>>()
        var currentList = mutableListOf<Int>()
        for (i in list) {
            if (i in neededNumbers) {
                listOfSubLists.add(currentList)
                currentList = mutableListOf()
            } else {
                currentList.add(i)
            }
        }
        listOfSubLists.add(currentList)

        val flattenedList = listOfSubLists.filter { it.isNotEmpty() }
        neededListOfSubLists.add(listOf(list.maxOrNull()!! + 3))
        val value = flattenedList.mapIndexed { index, it ->
            var sumForList = 0
            for (j in 0 until it.size + 1) {
                sumForList += subsets(it.toMutableList(), 0, j, 0)
                    .map { isValidList(listOf(neededListOfSubLists[index].last()) + it + listOf(neededListOfSubLists[index + 1][0])) }
                    .count { it }
            }
            sumForList
        }.fold(1L) { acc, i ->
            acc * i
        }
        println(value)
    }


    // Stolen from stackoverflow and modified to return the subsests instead of printing them
    fun subsets(arr: MutableList<Int>, pos: Int, depth: Int, startPos: Int): MutableList<MutableList<Int>> {
        val listList: MutableList<MutableList<Int>> = mutableListOf()
        var currentList: MutableList<Int> = mutableListOf()
        if (pos == depth) {
            for (i in 0 until depth) currentList.add(arr[i])
            listList.add(currentList)
            currentList = mutableListOf()
        }
        for (i in startPos until arr.size) {
            // optimization - not enough elements left
            if (depth - pos + i > arr.size) listList

            // swap pos and i
            var temp = arr[pos]
            arr[pos] = arr[i]
            arr[i] = temp
            listList.addAll(subsets(arr, pos + 1, depth, i + 1))

            // swap pos and i back - otherwise things just gets messed up
            temp = arr[pos]
            arr[pos] = arr[i]
            arr[i] = temp
        }
        return listList
    }


}