import Day11Test.Direction.*
import Day11Test.SeatStatus.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Day11Test {


    enum class SeatStatus {
        Empty, Taken, Floor, Outside
    }

    enum class Direction {
        UpLeft, Up, UpRight,
        Left, Right,
        DownLeft, Down, DownRight
    }

    private fun getOccupiedSeatsWhenStabe(input: String): Int {
        val seenMatrices = mutableSetOf<List<List<SeatStatus>>>()
        var matrix = getMatrix(input)
        while (matrix !in seenMatrices) {
            seenMatrices.add(matrix)
            matrix = matrix.mapIndexed { y, it ->
                it.mapIndexed { x, currentStatus ->
                    getNewSeatStatus2(currentStatus, getSeenSeats(matrix, x, y))
                }
            }

        }
        matrix.map { println(it) }
        return matrix.flatten().count { it == Taken }

    }

    private fun getNewSeatStatus2(currentSeatStatus: SeatStatus, firstSeenSeats: List<SeatStatus>): SeatStatus {
        return if (currentSeatStatus == Floor) Floor
        else if (currentSeatStatus == Empty && !firstSeenSeats.any { it == Taken }) Taken
        else if (currentSeatStatus == Taken && firstSeenSeats.count { it == Taken } >= 5) Empty
        else currentSeatStatus
    }

    private fun getNewSeatStatus(currentSeatStatus: SeatStatus, neighbours: List<SeatStatus>): SeatStatus {
        return if (currentSeatStatus == Floor) Floor
        else if (currentSeatStatus == Empty && !neighbours.any { it == Taken }) Taken
        else if (currentSeatStatus == Taken && neighbours.count { it == Taken } >= 4) Empty
        else currentSeatStatus

    }

    private fun getSeenSeats(matrix: List<List<SeatStatus>>, x: Int, y: Int): List<SeatStatus> {
        return listOf(
            getSeatInDirection(UpLeft, x, y, matrix),
            getSeatInDirection(Up, x, y, matrix),
            getSeatInDirection(UpRight, x, y, matrix),
            getSeatInDirection(Left, x, y, matrix),
            getSeatInDirection(Right, x, y, matrix),
            getSeatInDirection(DownLeft, x, y, matrix),
            getSeatInDirection(Down, x, y, matrix),
            getSeatInDirection(DownRight, x, y, matrix)
        )
    }

    private fun getNeighbours(matrix: List<List<SeatStatus>>, x: Int, y: Int): List<SeatStatus> {
        return listOf(
            matrix.getOrNull(y - 1)?.getOrNull(x - 1) ?: Outside,
            matrix.getOrNull(y - 1)?.getOrNull(x) ?: Outside,
            matrix.getOrNull(y - 1)?.getOrNull(x + 1) ?: Outside,
            matrix.getOrNull(y)?.getOrNull(x - 1) ?: Outside,
            matrix.getOrNull(y)?.getOrNull(x + 1) ?: Outside,
            matrix.getOrNull(y + 1)?.getOrNull(x - 1) ?: Outside,
            matrix.getOrNull(y + 1)?.getOrNull(x) ?: Outside,
            matrix.getOrNull(y + 1)?.getOrNull(x + 1) ?: Outside
        )
    }

    private fun getMatrix(input: String): List<List<SeatStatus>> {
        return input
            .lines()
            .map {
                it.map { c ->
                    when (c) {
                        'L' -> Empty
                        '.' -> Floor
                        '#' -> Taken
                        else -> throw IllegalArgumentException("Unknown instruction: $it")
                    }
                }
            }
    }

    @Test
    fun `test getNeighbours`() {
        val input =
            "L.LL.LL.LL\n" +
                    "LLLLLLL.LL\n" +
                    "L.L.L..L..\n" +
                    "LLLL.LL.LL\n" +
                    "L.LL.LL.LL\n" +
                    "L.LLLLL.LL\n" +
                    "..L.L.....\n" +
                    "LLLLLLLLLL\n" +
                    "L.LLLLLL.L\n" +
                    "L.LLLLL.LL"
        val matrix = getMatrix(input)

        assertEquals(listOf(Empty, Empty, Empty, Floor, Floor, Empty, Empty, Empty), getNeighbours(matrix, 2, 2))
        assertEquals(
            listOf(Outside, Outside, Outside, Outside, Floor, Outside, Empty, Empty),
            getNeighbours(matrix, 0, 0)
        )
    }

    @Test
    fun listEqualsTest() {
        assertEquals(listOf(Empty, Floor, Outside), listOf(Empty, Floor, Outside))
        assertTrue(setOf((listOf(Empty, Floor, Outside))).contains(listOf(Empty, Floor, Outside)))
    }

    @Test
    fun `test demo input`() {
        val input =
            "L.LL.LL.LL\n" +
                    "LLLLLLL.LL\n" +
                    "L.L.L..L..\n" +
                    "LLLL.LL.LL\n" +
                    "L.LL.LL.LL\n" +
                    "L.LLLLL.LL\n" +
                    "..L.L.....\n" +
                    "LLLLLLLLLL\n" +
                    "L.LLLLLL.L\n" +
                    "L.LLLLL.LL\n"
        assertEquals(26, getOccupiedSeatsWhenStabe(input))
    }

    @Test
    fun `test real input`() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day11.txt")
        println(getOccupiedSeatsWhenStabe(input))
    }


    @Test
    fun `test getSeatInDirection`() {
        val input =
            ".......#.\n" +
                    "...#.....\n" +
                    ".#.......\n" +
                    ".........\n" +
                    "..#L....#\n" +
                    "....#....\n" +
                    ".........\n" +
                    "#........\n" +
                    "...#....."
        val input2 =
            ".............\n" +
                    ".L.L.#.#.#.#.\n" +
                    "............."
        val input3 =
            ".##.##.\n" +
                    "#.#.#.#\n" +
                    "##...##\n" +
                    "...L...\n" +
                    "##...##\n" +
                    "#.#.#.#\n" +
                    ".##.##."
        val matrix = getMatrix(input3)
        assertEquals(Outside, getSeatInDirection(UpLeft, 3, 3, matrix))
        assertEquals(Outside, getSeatInDirection(Up, 3, 3, matrix))
        assertEquals(Outside, getSeatInDirection(UpRight, 3, 3, matrix))
        assertEquals(Outside, getSeatInDirection(Left, 3, 3, matrix))
        assertEquals(Outside, getSeatInDirection(Right, 3, 3, matrix))
        assertEquals(Outside, getSeatInDirection(DownLeft, 3, 3, matrix))
        assertEquals(Outside, getSeatInDirection(Down, 3, 3, matrix))
        assertEquals(Outside, getSeatInDirection(DownRight, 3, 3, matrix))
    }


    private fun getSeatInDirection(direction: Direction, x: Int, y: Int, matrix: List<List<SeatStatus>>): SeatStatus {
        val (newX, newY) = when (direction) {
            UpLeft -> Pair(x - 1, y - 1)
            Up -> Pair(x, y - 1)
            UpRight -> Pair(x + 1, y - 1)
            Left -> Pair(x - 1, y)
            Right -> Pair(x + 1, y)
            DownLeft -> Pair(x - 1, y + 1)
            Down -> Pair(x, y + 1)
            DownRight -> Pair(x + 1, y + 1)
        }
        val nextSeat = matrix.getOrNull(newY)?.getOrNull(newX) ?: Outside
        return if (nextSeat == Outside || nextSeat == Taken || nextSeat == Empty) nextSeat else getSeatInDirection(
            direction,
            newX,
            newY,
            matrix
        )
    }


    @Test
    fun `test getNewSeatStatus`() {
        assertEquals(Floor, getNewSeatStatus(Floor, listOf(Empty, Empty, Empty, Floor, Floor, Empty, Empty, Empty)))
        assertEquals(
            Taken,
            getNewSeatStatus(Empty, listOf(Outside, Outside, Outside, Outside, Floor, Outside, Empty, Empty))
        )
    }


}