import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class Day21 {

    @Test
    fun `test demo input`() {
        val input =
            "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n" +
                    "trh fvjkl sbzzf mxmxvkd (contains dairy)\n" +
                    "sqjhc fvjkl (contains soy)\n" +
                    "sqjhc mxmxvkd sbzzf (contains fish)"

        println(countTimesNonAllergenicItemAppears(input))

    }

    @Test
    fun `test real input`() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day21.txt")

        println(countTimesNonAllergenicItemAppears(input))


    }

    private fun countTimesNonAllergenicItemAppears(input: String): Int {

        val allIngredients = input.lines().map { parseFood(it) }.flatMap { it.values.first() }.toSet()
        val allergenMap = findAllergenMap(input)
        val nonAllergenics = allIngredients.filter { it !in allergenMap.values }.toSet()


        val allIngredientsList = input
            .lines()
            .map { parseFood(it) }
            .map { it.values.first() }

        val allIngredientsCount = mutableMapOf<String, Int>()
        allIngredientsList.forEach {
            it.forEach { ingredient ->
                allIngredientsCount[ingredient] = allIngredientsCount.getOrDefault(ingredient, 0) + 1
            }
        }
        return allIngredientsCount.filter { it.key in nonAllergenics }.values.sum()
    }


    @Test
    fun `test demo input part 2`() {
        val input =
            "mxmxvkd kfcds sqjhc nhms (contains dairy, fish)\n" +
                    "trh fvjkl sbzzf mxmxvkd (contains dairy)\n" +
                    "sqjhc fvjkl (contains soy)\n" +
                    "sqjhc mxmxvkd sbzzf (contains fish)"


        val allergenMap = findAllergenMap(input)
        println(allergenMap.toSortedMap().values.joinToString(","))
    }

    @Test
    fun `test real input part 2`() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day21.txt")


        val allergenMap = findAllergenMap(input)
        println(allergenMap.toSortedMap().values.joinToString(","))
    }


    private fun findAllergenMap(foods: String): MutableMap<String, String> {

        val foundPossibleAllergens = mutableMapOf<String, MutableSet<String>>()
        //First remove intersections
        foods
            .lines()
            .map { parseFood(it) }
            .forEach {
                it.keys.forEach { key ->
                    if (key in foundPossibleAllergens) {
                        val currentPossibleAllergens = foundPossibleAllergens[key]!!
                        foundPossibleAllergens[key] = currentPossibleAllergens.intersect(it.values.first()).toMutableSet()
                    } else {
                        //They are all the same
                        foundPossibleAllergens[key] = it.values.first().toMutableSet()
                    }
                }
            }

        val ingredientToAllergen = mutableMapOf<String,String>()
        while (foundPossibleAllergens.values.any { it.isNotEmpty() }) {

            val entryThatIsAnAllergen = foundPossibleAllergens.entries.first { it.value.size == 1 }
            val onlyOneIngredientForAllergen = entryThatIsAnAllergen.value.toList().first()

            ingredientToAllergen.put(entryThatIsAnAllergen.key,onlyOneIngredientForAllergen)

            foundPossibleAllergens.values.map {
                it.remove(onlyOneIngredientForAllergen)
            }

        }

        return ingredientToAllergen

    }

    @Test
    fun `test parse foodItem`() {

        assertEquals(
            mapOf(
                "fish" to setOf("mxmxvkd", "kfcds", "sqjhc", "nhms"),
                "dairy" to setOf("mxmxvkd", "kfcds", "sqjhc", "nhms")
            ), parseFood("mxmxvkd kfcds sqjhc nhms (contains dairy, fish)")
        )
        assertEquals(
            mapOf("dairy" to setOf("trh", "fvjkl", "sbzzf", "mxmxvkd")),
            parseFood("trh fvjkl sbzzf mxmxvkd (contains dairy)")
        )

    }

    private fun parseFood(input: String): Map<String, Set<String>> {
        val ingredients = input.split(" (contains ")[0].split(" ").toSet()
        return input.split(" (contains ")[1].replace(")", "").split(", ").map { it to ingredients }.toMap()
    }


}


