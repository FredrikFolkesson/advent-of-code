import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day7Test {


    @Test
    fun test_demo_input() {
        assertEquals(
            4, numberOfBagColorsThatCanContainGoldBag(
                ("light red bags contain 1 bright white bag, 2 muted yellow bags.\n" +
                        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.\n" +
                        "bright white bags contain 1 shiny gold bag.\n" +
                        "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.\n" +
                        "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.\n" +
                        "dark olive bags contain 3 faded blue bags, 4 dotted black bags.\n" +
                        "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.\n" +
                        "faded blue bags contain no other bags.\n" +
                        "dotted black bags contain no other bags.").lines()
            )
        )
    }

    @Test
    fun `test real input`() {
        println(numberOfBagColorsThatCanContainGoldBag(readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day7.txt")))
    }

    @Test
    fun `test real input part 2`() {
        println(numberOfBagsInsideGoldBag(readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day7.txt")))
    }


    @Test
    fun `test_demo_input part 2`() {
        assertEquals(
            126, numberOfBagsInsideGoldBag(
                ("shiny gold bags contain 2 dark red bags.\n" +
                        "dark red bags contain 2 dark orange bags.\n" +
                        "dark orange bags contain 2 dark yellow bags.\n" +
                        "dark yellow bags contain 2 dark green bags.\n" +
                        "dark green bags contain 2 dark blue bags.\n" +
                        "dark blue bags contain 2 dark violet bags.\n" +
                        "dark violet bags contain no other bags.").lines()
            )
        )
    }


    private fun numberOfBagsInsideGoldBag(bagRulesList: List<String>): Int {
        val bagRules = bagRulesList.map { getBagRuleWithNumberOfBagsDirectlyInside(it) }.toMap()
        return getNumberOfBagsInside("shiny gold", bagRules)
    }

    @Test
    fun `test getBagRuleWithNumberOfBagsDirectlyInside`() {
        assertEquals(
            Pair("faded blue", listOf<Pair<Int, String>>()),
            getBagRuleWithNumberOfBagsDirectlyInside("faded blue bags contain no other bags.")
        )
        assertEquals(
            Pair("bright white", listOf<Pair<Int, String>>(Pair(1, "shiny gold"))),
            getBagRuleWithNumberOfBagsDirectlyInside("bright white bags contain 1 shiny gold bag.")
        )
        assertEquals(
            Pair("muted yellow", listOf<Pair<Int, String>>(Pair(2, "shiny gold"), Pair(9, "faded blue"))),
            getBagRuleWithNumberOfBagsDirectlyInside("muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.")
        )
    }

    private fun getBagRuleWithNumberOfBagsDirectlyInside(bagRuleString: String): Pair<String, List<Pair<Int, String>>> {
        return bagRuleString.split(" bags contain ").let {
            val containedBags = if (it[1].equals("no other bags.")) listOf() else it[1].split(", ")
                .map { it.split(" ")[0].toInt() to it.split(" ")[1] + " " + it.split(" ")[2] }
            Pair(it[0], containedBags)
        }
    }

    private fun getBagRule(bagRuleString: String): Pair<String, List<String>> {
        return bagRuleString.split(" bags contain ").let {
            val containedBags = if (it[1].equals("no other bags.")) listOf() else it[1].split(", ")
                .map { it.split(" ")[1] + " " + it.split(" ")[2] }
            Pair(it[0], containedBags)
        }
    }

    @Test
    fun `test_demo_input part 3`() {
        assertEquals(
            32, numberOfBagsInsideGoldBag(
                ("flight red bags contain 1 bright white bag, 2 muted yellow bags.\n" +
                        "dark orange bags contain 3 bright white bags, 4 muted yellow bags.\n" +
                        "bright white bags contain 1 shiny gold bag.\n" +
                        "muted yellow bags contain 2 shiny gold bags, 9 faded blue bags.\n" +
                        "shiny gold bags contain 1 dark olive bag, 2 vibrant plum bags.\n" +
                        "dark olive bags contain 3 faded blue bags, 4 dotted black bags.\n" +
                        "vibrant plum bags contain 5 faded blue bags, 6 dotted black bags.\n" +
                        "faded blue bags contain no other bags.\n" +
                        "dotted black bags contain no other bags.").lines()
            )
        )
    }

    private fun getNumberOfBagsInside(bagType: String, bagRules: Map<String, List<Pair<Int, String>>>): Int {
        val containedBags = bagRules.getOrElse(bagType) {
            throw IllegalArgumentException("No bag of type $bagType found in map")
        }
        // val get:List<Pair<Int, String>> = bagRules[bagType] ?: listOf()
        return if (containedBags.isEmpty()) 0 else containedBags.sumOf {
            it.first + (it.first * getNumberOfBagsInside(
                it.second,
                bagRules
            ))
        }
    }

    private fun numberOfBagColorsThatCanContainGoldBag(bagRulesList: List<String>): Int {

        val bagRules = bagRulesList.map { getBagRule(it) }.toMap()
        return bagRules.keys.count { containsGoldBag(it, bagRules) }
    }

    @Test
    fun `test get bag rule`() {
        assertEquals(
            Pair("light red", listOf("bright white", "muted yellow")),
            getBagRule("light red bags contain 1 bright white bag, 2 muted yellow bags.")
        )
        assertEquals(Pair("faded blue", listOf()), getBagRule("faded blue bags contain no other bags."))
        assertEquals(
            Pair("bright white", listOf("shiny gold")),
            getBagRule("bright white bags contain 1 shiny gold bag.")
        )
    }


    @Test
    fun `test containsGoldBag`() {
        assertEquals(
            true, containsGoldBag(
                "light red", mapOf(
                    Pair("light red", listOf<String>("bright white", "muted yellow")),
                    Pair("bright white", listOf<String>("shiny gold")),
                    Pair("muted yellow", listOf<String>("shiny gold", "faded blue"))
                )
            )
        )
        assertEquals(
            true, containsGoldBag(
                "dark orange", mapOf(
                    Pair("dark orange", listOf<String>("bright white", "muted yellow")),
                    Pair("light red", listOf<String>("bright white", "muted yellow")),
                    Pair("bright white", listOf<String>("shiny gold")),
                    Pair("muted yellow", listOf<String>("shiny gold", "faded blue"))
                )
            )
        )
        assertEquals(true, containsGoldBag("bright white", mapOf(Pair("bright white", listOf<String>("shiny gold")))))
        assertEquals(
            true,
            containsGoldBag("muted yellow", mapOf(Pair("muted yellow", listOf<String>("shiny gold", "faded blue"))))
        )
        assertEquals(
            false, containsGoldBag(
                "shiny gold", mapOf(
                    Pair("shiny gold", listOf<String>("dark olive", "vibrant plum")),
                    Pair("dark olive", listOf<String>("faded blue", "dotted black")),
                    Pair("vibrant plum", listOf<String>("faded blue", "dotted black")),
                    Pair("muted yellow", listOf<String>("shiny gold", "faded blue")),
                    Pair("dotted black", listOf<String>()),
                    Pair("faded blue", listOf<String>())
                )
            )
        )
        assertEquals(
            false, containsGoldBag(
                "dark olive", mapOf(
                    Pair("dark olive", listOf<String>("faded blue", "dotted black")),
                    Pair("dotted black", listOf<String>()),
                    Pair("faded blue", listOf<String>())
                )
            )
        )
        assertEquals(
            false, containsGoldBag(
                "vibrant plum", mapOf(
                    Pair("vibrant plum", listOf<String>("faded blue", "dotted black")),
                    Pair("dotted black", listOf<String>()),
                    Pair("faded blue", listOf<String>())
                )
            )
        )
        assertEquals(false, containsGoldBag("dotted black", mapOf(Pair("dotted black", listOf<String>()))))
        assertEquals(false, containsGoldBag("faded blue", mapOf(Pair("faded blue", listOf<String>()))))
    }

    private fun containsGoldBag(bagType: String, bagRules: Map<String, List<String>>): Boolean {
        val containedBagTypes = bagRules.getOrElse(bagType) {
            throw IllegalArgumentException("No bag of type $bagType found in map")
        }
        return when {
            containedBagTypes.contains("shiny gold") -> true
            containedBagTypes.isEmpty() -> false
            else -> containedBagTypes.any { containsGoldBag(it, bagRules) }
        }
    }

    private fun numberOfBagsInsideGoldBag2(bagRulesList: List<String>): Int {
        val bagRules = bagRulesList.map { getBagRuleWithNumberOfBagsDirectlyInside(it) }.toMap()
        return getNumberOfBagsInside2("shiny gold", bagRules)
    }

    private fun getNumberOfBagsInside2(bagType: String, bagRules: Map<String, List<Pair<Int, String>>>): Int {
        val containedBags = bagRules.getOrElse(bagType) {
            throw IllegalArgumentException("No bag of type $bagType found in map")
        }
        return if (containedBags.isEmpty()) 0 else containedBags.sumOf {
            it.first + (it.first * getNumberOfBagsInside2(
                it.second,
                bagRules
            ))
        }
    }


}