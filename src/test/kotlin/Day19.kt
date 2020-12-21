import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

typealias Rule = Triple<List<Int>, List<Int>, String>

class Day19 {

    @Test
    fun `test parse rules`() {
        val rule: Rule = Triple(listOf(4, 1, 5), listOf(), "")
        assertEquals(mapOf<Int, Rule>(0 to rule), parseRules("0: 4 1 5"))
        println(
            parseRules(
                "0: 4 1 5\n" +
                        "1: 2 3 | 3 2\n" +
                        "2: 4 4 | 5 5\n" +
                        "3: 4 5 | 5 4\n" +
                        "4: \"a\"\n" +
                        "5: \"b\""
            )
        )
    }

    private fun parseRules(input: String): Map<Int, Rule> {
        return input.lines().map {
            val key = it.substringBefore(":").toInt()
            val value = it.substringAfter(": ")

            if (value.contains(" | ")) {
                value.split(" | ")[0]
                Pair(
                    key,
                    Triple(
                        value.split(" | ")[0].split(" ").map { it.toInt() },
                        value.split(" | ")[1].split(" ").map { it.toInt() },
                        ""
                    )
                )

            } else {
                if (value.contains("\"")) {
                    Pair(key, Triple(listOf<Int>(), listOf<Int>(), value.replace("\"", "")))
                } else {
                    Pair(key, Triple(value.split(" ").map { it.toInt() }, listOf<Int>(), ""))
                }
            }

        }.toMap()

    }


    @Test
    fun `test matching`() {
        val allRules = parseRules(
            "0: 4 1 5\n" +
                    "1: 2 3 | 3 2\n" +
                    "2: 4 4 | 5 5\n" +
                    "3: 4 5 | 5 4\n" +
                    "4: \"a\"\n" +
                    "5: \"b\""
        )
        val rules = allRules.get(0)!!
        assertTrue(matchesRule("aaaabbb", allRules, rules, listOf()))

    }


    @Test
    fun `test part 1 and 2`() {
        val fileAsString = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day19.txt")
//        val fileAsString = "0: 4 1 5\n" +
//                "1: 2 3 | 3 2\n" +
//                "2: 4 4 | 5 5\n" +
//                "3: 4 5 | 5 4\n" +
//                "4: \"a\"\n" +
//                "5: \"b\"\n\n"+
//                "aaaabb\naaabab\nabbabb\nabbbab\naabaab\naabbbb\nabaaab\nababbb"
        val allRules = parseRules(fileAsString.split("\n\n")[0])
        println(allRules)

        println(fileAsString.split("\n\n")[1]
            .lines()
            .count {
//                matchesRulesNew(it, listOf(allRules[0]!!), allRules)
                matchesRule(it, allRules, allRules[8]!!, listOf(allRules[11]!!))
            })


    }


    // I do not know why this one does not work.
    private fun matchesRule(input: String, allRules: Map<Int, Rule>, rule: Rule, restOfRules: List<Rule>): Boolean {



        //Längden 1 är sista gången, då vi har vår sista regel
        if (input.length == 1) {
            return restOfRules.isEmpty() && input.first().toString() == rule.third
        }
        if (restOfRules.isEmpty()) {
            return false
        }

        if (rule.third.isNotEmpty()) {
            return if (input.first().toString() == rule.third) {
                matchesRule(input.drop(1), allRules, restOfRules.first(), restOfRules.drop(1))
            } else {
                false
            }

        }

        val nextRules = rule.first.map { allRules.get(it)!! } + restOfRules
        val resultOfFirst = matchesRule(input, allRules, nextRules.first(), nextRules.drop(1))


        var resultOfSecond = false
        if (rule.second.isNotEmpty()) {
            val nextRulesSecond = rule.second.map { allRules.get(it)!! } + restOfRules
            resultOfSecond = matchesRule(input, allRules, nextRulesSecond.first(), nextRulesSecond.drop(1))
        }
        return resultOfFirst || resultOfSecond


    }


    fun matchesRulesNew(msg: String, rules: List<Rule>, allRules: Map<Int, Rule>): Boolean {
        if (msg.length == 0) {
            return rules.isEmpty()
        }
        if (rules.isEmpty()) {
            return false
        }
        val first = rules.first()

        if (first.third.isNotEmpty()) {
            return if (msg.startsWith(first.third)) {
                matchesRulesNew(msg.drop(1), rules.drop(1), allRules)
            } else {
                false
            }
        }

        val resultOfFirst = matchesRulesNew(msg, first.first.map { allRules.get(it)!! } + rules.drop(1), allRules)
        var resultOfSecond = false
        if (first.second.isNotEmpty()) {
            resultOfSecond = matchesRulesNew(msg, first.second.map { allRules.get(it)!! } + rules.drop(1), allRules)
        }
        return resultOfFirst || resultOfSecond
    }

}


