import org.junit.jupiter.api.Test
import java.io.File
import kotlin.test.assertEquals

class Day6Test {


    @Test
    fun test_demo_input() {
        assertEquals(
            11, getTotalNumberOfQuestionsThatAnyoneAnsweredYesOn(
                ("abc\n" +
                        "\n" +
                        "a\n" +
                        "b\n" +
                        "c\n" +
                        "\n" +
                        "ab\n" +
                        "ac\n" +
                        "\n" +
                        "a\n" +
                        "a\n" +
                        "a\n" +
                        "a\n" +
                        "\n" +
                        "b")
            )
        )

    }

    @Test
    fun test_demo_input_strict() {
        assertEquals(
            6, getTotalNumberOfQuestionsThatEveryoneInAGroupAnsweredYesOn(
                ("abc\n" +
                        "\n" +
                        "a\n" +
                        "b\n" +
                        "c\n" +
                        "\n" +
                        "ab\n" +
                        "ac\n" +
                        "\n" +
                        "a\n" +
                        "a\n" +
                        "a\n" +
                        "a\n" +
                        "\n" +
                        "b")
            )
        )

    }

    @Test
    fun test_real_input() {
        print(getTotalNumberOfQuestionsThatAnyoneAnsweredYesOn(readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day6.txt")))
    }

    @Test
    fun test_real_input_part_2() {
        print(getTotalNumberOfQuestionsThatEveryoneInAGroupAnsweredYesOn(readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day6.txt")))

    }


    private fun getTotalNumberOfQuestionsThatAnyoneAnsweredYesOn(input: String): Int {
        return input.split("\n\n").sumBy { getNumberOfYesAnswersForAnyQuestionForGroup(it) }
    }

    private fun getNumberOfYesAnswersForAnyQuestionForGroup(input: String): Int {
        return input.replace("\n", "").fold(setOf<Char>()) { set, yesAnswer ->
            set.plus(yesAnswer)
        }.size
    }

    private fun getTotalNumberOfQuestionsThatEveryoneInAGroupAnsweredYesOn(input: String): Int {
        return input.split("\n\n").sumBy { getNumberOfQuestionsThatEveryoneInTheGroupAnsweredYesOn(it) }
    }

    private fun getNumberOfQuestionsThatEveryoneInTheGroupAnsweredYesOn(input: String): Int {
        return input
            .replace("\n", "")
            .fold(mapOf<Char, Int>()) { map, yesAnswer ->
                map + Pair(yesAnswer, map.getOrDefault(yesAnswer, 0) + 1)
            }
            .count { it.value == input.lines().size }
    }


    object Task2 {

        fun sumOfPositiveAnswers2(input: String): Int {
            return input
                .split("\n\n")
                .map { it.split("\n") }
                .map { list -> list.map { it.toSet() } }
                .map { it.fold(it[0]) { set, n -> set.intersect(n) } }
                .sumOf { it.size }
        }
    }


    @Test
    fun `test group answeres`() {
        assertEquals(4, getNumberOfYesAnswersForAnyQuestionForGroup("abcx"))
        assertEquals(4, getNumberOfYesAnswersForAnyQuestionForGroup("abcy"))
        assertEquals(4, getNumberOfYesAnswersForAnyQuestionForGroup("abcz"))
        assertEquals(3, getNumberOfYesAnswersForAnyQuestionForGroup("abc"))
        assertEquals(
            3, getNumberOfYesAnswersForAnyQuestionForGroup(
                "a\n" +
                        "b\n" +
                        "c"
            )
        )
        assertEquals(
            3, getNumberOfYesAnswersForAnyQuestionForGroup(
                "ab\n" +
                        "ac"
            )
        )
        assertEquals(
            1, getNumberOfYesAnswersForAnyQuestionForGroup(
                "a\n" +
                        "a\n" +
                        "a\n" +
                        "a"
            )
        )
        assertEquals(1, getNumberOfYesAnswersForAnyQuestionForGroup("b"))
    }

//    private fun getTotalNumberOfQuestionsThatAnyoneAnsweredYesOn(input: String): Int {
//        return input.split("\n\n").sumBy { getNumberOfYesAnswersForAnyQuestionForGroup(it)  }
//    }


//    private fun getNumberOfYesAnswersForAnyQuestionForGroup(input: String): Int {
//        return input.replace("\n", "").fold(setOf<Char>()) {
//                set, yesAnswer -> set.plus(yesAnswer)
//        }.size
//    }

    object Task1 {
        fun sumOfPositiveAnswers(input: String): Int {
            return input
                .split("\n\n")
                .map { it.replace("\n", "") }
                .sumOf { it.toSet().size }
        }
    }


    fun readFileAsLinesUsingUseLines(fileName: String): List<String> = File(fileName).useLines { it.toList() }
    fun readFileAsString(fileName: String): String = File(fileName).readText()


}