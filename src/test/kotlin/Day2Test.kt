import Day2.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day2Test {


    @Test
    fun `find_total_number_of_correct_passwords`() {
        // Given
        val input = "1-3 a: abcde\n" +
                "1-3 b: cdefg\n" +
                "2-9 c: ccccccccc"
        val day2 = Day2()

        // When
        val totalCorrectNumberOfPasswords = day2.findTotalNumberOfCorrectPassword(input.lines())

        // Then
        assertEquals(1, totalCorrectNumberOfPasswords)
    }

    @Test
    fun `find_total_number_of_correct_passwords_in_real_input`() {
        // Given
        val lines =
            readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day2.txt")
        val day2 = Day2()

        // When
        val totalCorrectNumberOfPasswords = day2.findTotalNumberOfCorrectPassword(lines)

        println(totalCorrectNumberOfPasswords)
    }


    @Test
    fun `should_be_valid_password_with_first_validator`() {
        assertTrue { PasswordAndValidator("1-3 a: abcde").isValid1() }
        assertTrue { PasswordAndValidator("2-9 c: ccccccccc").isValid1() }
    }

    @Test
    fun `should_be_invalid_password_with_first_validator`() {
        assertFalse { PasswordAndValidator("1-3 b: cdefg").isValid1() }
    }

    @Test
    fun `should_be_valid_password_with_second_validator`() {
        assertTrue { PasswordAndValidator("1-3 a: abcde").isValid2() }
    }

    @Test
    fun `should_be_invalid_password_with_second_validator`() {
        assertFalse { PasswordAndValidator("1-3 b: cdefg").isValid2() }
        assertFalse { PasswordAndValidator("2-9 c: ccccccccc").isValid2() }
    }

}