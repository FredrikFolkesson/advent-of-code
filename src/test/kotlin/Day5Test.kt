import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day5Test {


    @Test
    fun test_real_input() {
        println(getHighestSeatNumber(readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day5.txt")))
    }

    @Test
    fun test_real_input_part_2() {
        println(findMissingBoardingPass(readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day5.txt")))
    }

    private fun findMissingBoardingPass(boardingpasses: List<String>): Int {
        val sortedBoardingPassSeatIds =
            boardingpasses.map { boardingpass -> getSeatId(getRow(boardingpass), getColumn(boardingpass)) }.sorted()
        return (sortedBoardingPassSeatIds[0]..sortedBoardingPassSeatIds[sortedBoardingPassSeatIds.size - 1]).filter { !(it in sortedBoardingPassSeatIds) }
            .first()
    }

    private fun getHighestSeatNumber(boardingpasses: List<String>): Int {
        return boardingpasses.maxOf { boardingpass -> getSeatId(getRow(boardingpass), getColumn(boardingpass)) }
    }

    @Test
    fun test_seat_id() {
        assertEquals(567, getSeatId(70, 7))
        assertEquals(119, getSeatId(14, 7))
        assertEquals(820, getSeatId(102, 4))
    }

    @Test
    fun test_seat_id_with_boarding_pass() {
        assertEquals(567, getSeatId(getRow("BFFFBBFRRR"), getColumn("BFFFBBFRRR")))
        assertEquals(119, getSeatId(getRow("FFFBBBFRRR"), getColumn("FFFBBBFRRR")))
        assertEquals(820, getSeatId(getRow("BBFFBBFRLL"), getColumn("BBFFBBFRLL")))
    }

    @Test
    fun test_row() {
        assertEquals(44, getRow("FBFBBFFRLR"))
        assertEquals(70, getRow("BFFFBBFRRR"))
        assertEquals(14, getRow("FFFBBBFRRR"))
        assertEquals(102, getRow("BBFFBBFRLL"))

    }

    @Test
    fun test_column() {
        assertEquals(5, getColumn("FBFBBFFRLR"))
        assertEquals(7, getColumn("BFFFBBFRRR"))
        assertEquals(7, getColumn("FFFBBBFRRR"))
        assertEquals(4, getColumn("BBFFBBFRLL"))

    }

    private fun getColumn(input: String): Int {
        val columnPart = input.substring(7)
        return doBinarySpacePartitioning(columnPart, 0, 7, 'L')
    }

    private fun getRow(input: String): Int {
        val rowPart = input.substring(0, 7)
        val rowBinaryString = rowPart.replace("F", "0").replace("L", "1").replace("L", "0").replace("B", "1")
        return rowBinaryString.toInt(2)
        //return doBinarySpacePartitioning(rowPart, 0,127, 'F')
    }

    private fun doBinarySpacePartitioning(input: String, initialMin: Int, initialMax: Int, lowerHalfChar: Char): Int {
        return input.fold(Pair(initialMin, initialMax)) { (min, max), char ->
            if (char == lowerHalfChar) {
                Pair(min, min + (max - min) / 2)
            } else {
                Pair(min + (max - min) / 2, max)
            }
        }.second
    }

    private fun getSeatId(row: Int, column: Int): Int = row * 8 + column

}