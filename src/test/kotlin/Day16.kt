import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day16 {


    @Test
    fun `test creation of allowed numbers set`() {
        assertEquals(6, getAllowedNumbers("class: 1-3 or 5-7").size)
        assertEquals(
            48, getAllowedNumbers(
                "class: 1-3 or 5-7\n" +
                        "row: 6-11 or 33-44\n" +
                        "seat: 13-40 or 45-50"
            ).size
        )
    }

    @Test
    fun `test demo input part 1`() {
        val input = "class: 1-3 or 5-7\n" +
                "row: 6-11 or 33-44\n" +
                "seat: 13-40 or 45-50\n" +
                "\n" +
                "your ticket:\n" +
                "7,1,14\n" +
                "\n" +
                "nearby tickets:\n" +
                "7,3,47\n" +
                "40,4,50\n" +
                "55,2,20\n" +
                "38,6,12"
        assertEquals(71, getScanningError(input))
    }

    @Test
    fun `test demo input part 2`() {
        val input = "class: 0-1 or 4-19\n" +
                "row: 0-5 or 8-19\n" +
                "seat: 0-13 or 16-19\n" +
                "\n" +
                "your ticket:\n" +
                "11,12,13\n" +
                "\n" +
                "nearby tickets:\n" +
                "3,9,18\n" +
                "15,1,5\n" +
                "5,14,9"
        assertEquals(mapOf("class" to 1, "row" to 0, "seat" to 2), getFieldNameToIndex(input))
    }

    @Test
    fun `part 2`() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day16.txt")
        val myTicket = getMyTicket(input)
        val fieldNameToIndex = getFieldNameToIndex(input)
        val finalValue = fieldNameToIndex.entries.fold(1L) { currentValue, (key, index) ->
            if (key.startsWith("departure")) {
                currentValue * myTicket[index]
            } else {
                currentValue
            }
        }
        println(finalValue)

    }

    private fun getMyTicket(input: String): List<Int> {
        val blankLineSplittedInput = input.split("\n\n")
        return blankLineSplittedInput.get(1).lines().drop(1).get(0).split(",").map { it.toInt() }
    }

    private fun getFieldNameToIndex(input: String): Map<String, Int> {
        val blankLineSplittedInput = input.split("\n\n")
        val allowedNumbers = getAllowedNumbers(blankLineSplittedInput.first())
        val validTickets = getValidTickets(blankLineSplittedInput.last(), allowedNumbers)

        val fieldToAllowedNumbers = fieldNameToAllowedNumbers(blankLineSplittedInput.first()).toMutableMap()

        val listOfSetByColumns = mutableListOf<Pair<Int, MutableSet<Int>>>()
        validTickets[0].split(",").forEachIndexed { index, it -> listOfSetByColumns.add(Pair(index, mutableSetOf())) }
        validTickets.fold(listOfSetByColumns.toList()) { acc, ticket ->
            ticket.split(",").mapIndexed { index, it -> acc[index].second.add(it.toInt()) }
            acc
        }

        val fieldNameToIndex = mutableMapOf<String, Int>()

        while (listOfSetByColumns.isNotEmpty()) {

            val (fieldName, matchingColumns) = fieldToAllowedNumbers.map { (fieldName, allowedNumbersForField) ->
                Pair(fieldName, listOfSetByColumns.filter {
                    allowedNumbersForField.containsAll(it.second)
                })
            }.first { it.second.size == 1 }

            listOfSetByColumns.remove(matchingColumns.first())
            fieldNameToIndex[fieldName] = matchingColumns.first().first

        }
        return fieldNameToIndex
    }

    private fun fieldNameToAllowedNumbers(input: String): Map<String, Set<Int>> {
        return input.lines().fold(mutableMapOf<String, Set<Int>>()) { map, it ->
            val key = it.split(": ")[0]

            val setOfAllowedNumberForKey = it.substringAfter(": ").split(" or ").map {
                val numbers = it.split("-")
                (numbers[0].toInt()..numbers[1].toInt()).toSet()
            }.fold(setOf<Int>()) { set, newSet -> set + newSet }

            map.put(key, setOfAllowedNumberForKey)
            map

        }.toMap()
    }


    @Test
    fun `test real input`() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day16.txt")
        println(getScanningError(input))
    }

    private fun getScanningError(input: String): Int {
        val blankLineSplittedInput = input.split("\n\n")

        val allowedNumbers = getAllowedNumbers(blankLineSplittedInput.first())
        val valuesOnNearbyTickets = getValuesOnNearbyTickets(blankLineSplittedInput.last())
        return valuesOnNearbyTickets.filter { it !in allowedNumbers }.sum()
    }

    private fun getValidTickets(nearbyTickets: String, allowedNumbers: Set<Int>): List<String> {
        return nearbyTickets.lines().drop(1).filter { it.split(",").filter { it.toInt() !in allowedNumbers }.isEmpty() }

    }

    private fun getValuesOnNearbyTickets(nearbyTickets: String): List<Int> {
        return nearbyTickets.lines().drop(1).fold(listOf<Int>()) { list, line ->
            list + line.split(",").map { it.toInt() }
        }

    }

    private fun getAllowedNumbers(input: String): Set<Int> {
        return input.lines().fold(setOf<Int>()) { set, it ->
            it.substringAfter(": ").split(" or ").map {
                val numbers = it.split("-")
                (numbers[0].toInt()..numbers[1].toInt()).toSet()
            }.fold(set) { set, newSet -> set + newSet }
        }
    }

    @Test
    fun `test ranges are inclusive on end element`() {
        assertEquals(10, (1..10).toSet().size)
    }


}