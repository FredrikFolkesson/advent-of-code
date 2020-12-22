import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class Day22 {

    @Test
    fun `test demo input parsing`() {
        val input = "" +
                "Player 1:\n" +
                "9\n" +
                "2\n" +
                "6\n" +
                "3\n" +
                "1\n" +
                "\n" +
                "Player 2:\n" +
                "5\n" +
                "8\n" +
                "4\n" +
                "7\n" +
                "10"

        assertEquals(
            Pair<List<Int>, List<Int>>(listOf(9, 2, 6, 3, 1), listOf(5, 8, 4, 7, 10)),
            getCardForPlayers(input)
        )

    }

    private fun getCardForPlayers(input: String): Pair<List<Int>, List<Int>> {
        val pairAsList = input.split("\n\n")
            .map { player ->
                player
                    .lines()
                    .drop(1)
                    .map { it.toInt() }
            }
        return Pair(pairAsList.first(), pairAsList.last())
    }

    @Test
    fun `test demo input`() {
        val input = "" +
                "Player 1:\n" +
                "9\n" +
                "2\n" +
                "6\n" +
                "3\n" +
                "1\n" +
                "\n" +
                "Player 2:\n" +
                "5\n" +
                "8\n" +
                "4\n" +
                "7\n" +
                "10"

        val (playerOneCards, playerTwoCards) = getCardForPlayers(input)
        assertEquals(
            listOf(3, 2, 10, 6, 8, 5, 9, 4, 7, 1),
            playCrabCombat(playerOneCards.toMutableList(), playerTwoCards.toMutableList())
        )
        assertEquals(306, countScore(playCrabCombat(playerOneCards.toMutableList(), playerTwoCards.toMutableList())))
    }


    @Test
    fun `test demo input part 2`() {
        val input = "" +
                "Player 1:\n" +
                "9\n" +
                "2\n" +
                "6\n" +
                "3\n" +
                "1\n" +
                "\n" +
                "Player 2:\n" +
                "5\n" +
                "8\n" +
                "4\n" +
                "7\n" +
                "10"

        val (playerOneCards, playerTwoCards) = getCardForPlayers(input)
        val (playerOneCardsAtEnd, playerTwoCardsAtEnd) = playCrabCombatRecursive(
            playerOneCards.toMutableList(),
            playerTwoCards.toMutableList()
        )
        assertEquals(291, countScore(listOf(playerOneCardsAtEnd, playerTwoCardsAtEnd).first { it.isNotEmpty() }))
    }

    private fun playCrabCombatRecursive(
        playerOneInitialCards: List<Int>,
        playerTwoInitialCards: List<Int>
    ): Pair<List<Int>, List<Int>> {
        val playerOneCards = playerOneInitialCards.toMutableList()
        val playerTwoCards = playerTwoInitialCards.toMutableList()
        val seenRounds = mutableSetOf<Pair<List<Int>, List<Int>>>()

        while (playerOneCards.isNotEmpty() && playerTwoCards.isNotEmpty()) {
            val state = Pair(playerOneCards.toList(), playerTwoCards.toList())

            val playerOneCard = playerOneCards.removeFirst()
            val playerTwoCard = playerTwoCards.removeFirst()
            if (seenRounds.contains(state)) {
                //avsluta spelet inte rundan, vi behöver bara ge spelare ett en icke tom lista så han blir en vinnare, oklart vad som skulle vara korrekt ske om detta var det yttersta spelet
                return Pair(listOf(1), listOf())

            } else {
                seenRounds.add(state)
                if (playerOneCards.size >= playerOneCard && playerTwoCards.size >= playerTwoCard) {



                    val (playerOneCardsAfterRecursive, _) = playCrabCombatRecursive(
                        playerOneCards.take(playerOneCard),
                        playerTwoCards.take(playerTwoCard)
                    )
                    if (playerOneCardsAfterRecursive.isNotEmpty()) {
                        playerOneCards.add(playerOneCard)
                        playerOneCards.add(playerTwoCard)
                    } else {
                        playerTwoCards.add(playerTwoCard)
                        playerTwoCards.add(playerOneCard)
                    }
                } else {
                    if (playerOneCard > playerTwoCard) {
                        playerOneCards.add(playerOneCard)
                        playerOneCards.add(playerTwoCard)
                    } else {
                        playerTwoCards.add(playerTwoCard)
                        playerTwoCards.add(playerOneCard)
                    }
                }

            }


        }
        return Pair(playerOneCards, playerTwoCards)
    }


    @Test
    fun `real input part 1`() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day22.txt")
        val (playerOneCards, playerTwoCards) = getCardForPlayers(input)
        val winningCards = playCrabCombat(playerOneCards.toMutableList(), playerTwoCards.toMutableList())
        println(countScore(winningCards))
    }

    @Test
    fun `real input part 2`() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day22.txt")
        val (playerOneCards, playerTwoCards) = getCardForPlayers(input)
        val (playerOneCardsAtEnd, playerTwoCardsAtEnd) = playCrabCombatRecursive(
            playerOneCards.toMutableList(),
            playerTwoCards.toMutableList()
        )
        println(countScore(listOf(playerOneCardsAtEnd, playerTwoCardsAtEnd).first { it.isNotEmpty() }))
    }

    @Test
    fun `handles game that would infinitely loop without loop rule`() {
        val input = "Player 1:\n" +
                "43\n" +
                "19\n" +
                "\n" +
                "Player 2:\n" +
                "2\n" +
                "29\n" +
                "14"
        val (playerOneCards, playerTwoCards) = getCardForPlayers(input)
        val (playerOneCardsAtEnd, playerTwoCardsAtEnd) = playCrabCombatRecursive(
            playerOneCards.toMutableList(),
            playerTwoCards.toMutableList()
        )
        println(countScore(listOf(playerOneCardsAtEnd, playerTwoCardsAtEnd).first { it.isNotEmpty() }))
    }

    private fun countScore(cards: List<Int>): Long {
        return cards.reversed().foldIndexed(0L) { index, acc, i ->
            acc + (i * (index + 1))
        }

    }

    private fun playCrabCombat(playerOneCards: MutableList<Int>, playerTwoCards: MutableList<Int>): List<Int> {
        while (playerOneCards.isNotEmpty() && playerTwoCards.isNotEmpty()) {
            val playerOneCard = playerOneCards.removeFirst()
            val playerTwoCard = playerTwoCards.removeFirst()

            //Vinnarens kort över
            if (playerOneCard > playerTwoCard) {
                playerOneCards.add(playerOneCard)
                playerOneCards.add(playerTwoCard)
            } else {
                playerTwoCards.add(playerTwoCard)
                playerTwoCards.add(playerOneCard)
            }

        }
        return listOf(playerOneCards, playerTwoCards).first { it.isNotEmpty() }
    }


    @Test
    fun testMutability() {
        val a = mutableListOf(1, 2, 3)
        iMADropper(a)
        println(a)
    }

    fun iMADropper(list: List<Int>) {
        val nowMutable = list.toMutableList()
        nowMutable.removeFirst()
    }


}


