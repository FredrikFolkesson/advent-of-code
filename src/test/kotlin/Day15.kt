import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day15 {



    @Test
    fun `demo input`() {
        assertEquals(436, getSpokenNumberAtTurn(2020, listOf(0,3,6)))
    }

    @Test
    fun `test real input`() {
        println(getSpokenNumberAtTurn(30000000, listOf(16,11,15,0,1,7)))
    }

    private fun getSpokenNumberAtTurn(maxTurn: Int, startingNumbers: List<Int>): Int {
        val lastSpokenMap = startingNumbers.mapIndexed { index,it -> it to Pair(0, index+1) }.toMap()


        return (startingNumbers.size+1..maxTurn).fold(Pair(lastSpokenMap, startingNumbers[startingNumbers.size-1])) { (lastSpokenMap, lastNumberSpoken), turn ->

            val lastTimeSpoken = lastSpokenMap.getOrDefault(lastNumberSpoken, Pair(0,turn))
            val numberToSpeak = if (lastTimeSpoken.first == 0) 0 else lastTimeSpoken.second - lastTimeSpoken.first
            val updatedNumberToSpeak = if (lastSpokenMap.containsKey(numberToSpeak)) Pair(lastSpokenMap.get(numberToSpeak)!!.second , turn) else Pair(0, turn)
            Pair(lastSpokenMap + (numberToSpeak to updatedNumberToSpeak), numberToSpeak)

        }.second

    }

    private fun getSpokenNumberAtTurnPartUsingMutableMapForSpeed(maxTurn: Long, startingNumbers: List<Long>): Long {
        val lastSpokenMap = startingNumbers.mapIndexed { index,it -> it to Pair(0L, index.toLong()+1L) }.toMap().toMutableMap()


        return (startingNumbers.size+1..maxTurn).fold(Pair(lastSpokenMap, startingNumbers[startingNumbers.size-1])) { (lastSpokenMap, lastNumberSpoken), turn ->
            val lastTimeSpoken = lastSpokenMap.getOrDefault(lastNumberSpoken, Pair(0,turn))
            val numberToSpeak = if (lastTimeSpoken.first == 0L) 0L else lastTimeSpoken.second - lastTimeSpoken.first

            val updatedNumberToSpeak = if (lastSpokenMap.containsKey(numberToSpeak)) Pair(lastSpokenMap.get(numberToSpeak)!!.second , turn) else Pair(0L, turn)
            lastSpokenMap[numberToSpeak] = updatedNumberToSpeak
            Pair(lastSpokenMap, numberToSpeak)

        }.second

    }

}