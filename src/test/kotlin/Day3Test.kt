import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day3Test {


    @Test
    fun `test_demo_input`() {

        val input =
            "..##.......\n" +
                    "#...#...#..\n" +
                    ".#....#..#.\n" +
                    "..#.#...#.#\n" +
                    ".#...##..#.\n" +
                    "..#.##.....\n" +
                    ".#.#.#....#\n" +
                    ".#........#\n" +
                    "#.##...#...\n" +
                    "#...##....#\n" +
                    ".#..#...#.#"


        val day3 = Day3()
        assertEquals(7, day3.getNumberOfTrees(input.lines(), 1, 2))
    }

    @Test
    fun test_first_input() {

        val input =
            readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day3.txt")

        val day3 = Day3()
        assertEquals(254, day3.getNumberOfTrees(input, 3, 1))
    }


}