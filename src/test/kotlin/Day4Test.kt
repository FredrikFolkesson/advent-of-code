import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Day4Test {


    @Test
    fun test_demo_input() {

        val input = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
                "byr:1937 iyr:2017 cid:147 hgt:183cm\n" +
                "\n" +
                "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
                "hcl:#cfa07d byr:1929\n" +
                "\n" +
                "hcl:#ae17e1 iyr:2013\n" +
                "eyr:2024\n" +
                "ecl:brn pid:760753108 byr:1931\n" +
                "hgt:179cm\n" +
                "\n" +
                "hcl:#cfa07d eyr:2025 pid:166559648\n" +
                "iyr:2011 ecl:brn hgt:59in"
        countValidPassword(input)
    }

    @Test
    fun test_real_input() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day4.txt")
        countValidPassword(input)
    }


    private fun countValidPassword(input: String) {
        println(input.split("\n\n").map {
            isPasswordValidStrict(createPasswordMapFromString(it))
        }.filter { it }.count())
    }

    private fun isPasswordValidStrict(passwordMap: Map<String, String>): Boolean {
        val byr = passwordMap.getOrDefault("byr", "0").toInt()
        val iyr = passwordMap.getOrDefault("iyr", "0").toInt()
        val eyr = passwordMap.getOrDefault("eyr", "0").toInt()

        val hgt = passwordMap.getOrDefault("hgt", "")
        val hgtValid = when {
            "cm" in hgt -> {
                hgt.replace("cm", "").toInt() in 150..193
            }
            "in" in hgt -> {
                hgt.replace("in", "").toInt() in 59..76
            }
            else -> false
        }

        val hcl = passwordMap.getOrDefault("hcl", "").matches("^#[a-f0-9]{6}\$".toRegex())

        val ecl = passwordMap.getOrDefault("ecl", "")
        val eclValid =
            ecl.contains("amb") || ecl.contains("blu") || ecl.contains("brn") || ecl.contains("gry") || ecl.contains("grn") || ecl.contains(
                "hzl"
            ) || ecl.contains("oth")
        val pid = passwordMap.getOrDefault("pid", "").matches("^[0-9]{9}\$".toRegex())
        return byr in 1920..2002 && iyr >= 2010 && iyr <= 2020 && eyr >= 2020 && eyr <= 2030 && hgtValid && hcl && eclValid && pid
    }


    @Test
    fun is_password_valid() {
        assertTrue(
            isPasswordValid(
                createPasswordMapFromString(
                    "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
                            "byr:1937 iyr:2017 cid:147 hgt:183cm"
                )
            )
        )
        assertTrue(
            isPasswordValid(
                createPasswordMapFromString(
                    "hcl:#ae17e1 iyr:2013\n" +
                            "eyr:2024\n" +
                            "ecl:brn pid:760753108 byr:1931\n" +
                            "hgt:179cm"
                )
            )
        )
        assertFalse(
            isPasswordValid(
                createPasswordMapFromString(
                    "iyr:2013 ecl:amb cid:350 eyr:2023 pid:028048884\n" +
                            "hcl:#cfa07d byr:1929"
                )
            )
        )
        assertFalse(
            isPasswordValid(
                createPasswordMapFromString(
                    "hcl:#cfa07d eyr:2025 pid:166559648\n" +
                            "iyr:2011 ecl:brn hgt:59in"
                )
            )
        )
    }

    @Test
    fun strict_is_password_valid() {
        assertTrue(
            isPasswordValidStrict(
                createPasswordMapFromString(
                    "pid:087499704 hgt:74in ecl:grn iyr:2012 eyr:2030 byr:1980\n" +
                            "hcl:#623a2f"
                )
            )
        )
        assertTrue(
            isPasswordValidStrict(
                createPasswordMapFromString(
                    "eyr:2029 ecl:blu cid:129 byr:1989\n" +
                            "iyr:2014 pid:896056539 hcl:#a97842 hgt:165cm"
                )
            )
        )
        assertTrue(
            isPasswordValidStrict(
                createPasswordMapFromString(
                    "hcl:#888785\n" +
                            "hgt:164cm byr:2001 iyr:2015 cid:88\n" +
                            "pid:545766238 ecl:hzl\n" +
                            "eyr:2022"
                )
            )
        )

        assertTrue(isPasswordValidStrict(createPasswordMapFromString("iyr:2010 hgt:158cm hcl:#b6652a ecl:blu byr:1944 eyr:2021 pid:093154719")))
    }

    @Test
    fun strict_is_password_invalid() {
        assertFalse(
            isPasswordValidStrict(
                createPasswordMapFromString(
                    "eyr:1972 cid:100\n" +
                            "hcl:#18171d ecl:amb hgt:170 pid:186cm iyr:2018 byr:1926"
                )
            )
        )
        assertFalse(
            isPasswordValidStrict(
                createPasswordMapFromString(
                    "iyr:2019\n" +
                            "hcl:#602927 eyr:1967 hgt:170cm\n" +
                            "ecl:grn pid:012533040 byr:1946"
                )
            )
        )
        assertFalse(
            isPasswordValidStrict(
                createPasswordMapFromString(
                    "hcl:dab227 iyr:2012\n" +
                            "ecl:brn hgt:182cm pid:021572410 eyr:2020 byr:1992 cid:277"
                )
            )
        )
        assertFalse(
            isPasswordValidStrict(
                createPasswordMapFromString(
                    "hgt:59cm ecl:zzz\n" +
                            "eyr:2038 hcl:74454a iyr:2023\n" +
                            "pid:3556412378 byr:2007"
                )
            )
        )
    }


    @Test
    fun convert_string_to_password_map() {
        val givenPasswordMap = mapOf<String, Any>(
            Pair("ecl", "gry"),
            Pair("pid", 860033327),
            Pair("eyr", 2020),
            Pair("hcl", "#fffffd"),
            Pair("byr", 1937),
            Pair("iyr", 2017),
            Pair("cid", 147),
            Pair("hgt", "183cm")
        )
        val inputString = "ecl:gry pid:860033327 eyr:2020 hcl:#fffffd\n" +
                "byr:1937 iyr:2017 cid:147 hgt:183cm"
        assertEquals(givenPasswordMap, createPasswordMapFromString(inputString))
    }

    @Test
    fun `map test`() {
        listOf<String>("AAA bbb", "CCC DDDD").map { it.split(" ") }.map { println(it) }
    }

    private fun createPasswordMapFromString(inputString: String): Map<String, String> {
        val passwordMap = mutableMapOf<String, String>()
        inputString.lines().map {
            it.split(" ").map {
                val splittedKeyValue = it.split(":")
                passwordMap.put(splittedKeyValue[0], splittedKeyValue[1])
            }
        }
        return passwordMap
    }

    private fun isPasswordValid(passwordMap: Map<String, String>): Boolean {
        return passwordMap.containsKey("ecl") && passwordMap.containsKey("pid") && passwordMap.containsKey("eyr") && passwordMap.containsKey(
            "hcl"
        ) && passwordMap.containsKey("byr") && passwordMap.containsKey("iyr") && passwordMap.containsKey("hgt")
    }

}




