import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day13 {


    @Test
    fun `test demo input`() {
        val earliestTimestamp = "939"
        val busIds = "7,13,x,x,59,x,31,19"
        assertEquals(295, findBussIdTimesLowestWaitTime(earliestTimestamp, busIds))
    }

    @Test
    fun `test real input`() {
        val earliestTimestamp = "1002561"
        val busIds = "17,x,x,x,x,x,x,x,x,x,x,37,x,x,x,x,x,409,x,29,x,x,x,x,x,x,x,x,x,x,13,x,x,x,x,x,x,x,x,x,23,x,x,x,x,x,x,x,373,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,19"
        println(findBussIdTimesLowestWaitTime(earliestTimestamp, busIds))
    }

    private fun findBussIdTimesLowestWaitTime(earliestTimestampString: String, busIds: String): Int {
        val earliestTimestamp = earliestTimestampString.toInt()
        val buslines = busIds.split(",").filter { it != "x" }.map { it.toInt() }
        val (busId, earliestPossibleLeavingBusTime) = buslines
            .map { findEarliestPossibleBusTimeForBusLine(earliestTimestamp, it) }
            .minByOrNull {
                it.second
            } ?: throw Error("No bus times found")
        return (earliestPossibleLeavingBusTime-earliestTimestamp) * busId
    }

    @Test
    fun `test findEarliestTime`() {
        assertEquals(944,findEarliestPossibleBusTimeForBusLine(939, 59))
    }

    private fun findEarliestPossibleBusTimeForBusLine(earliestPossibleTimestamp: Int, bussId: Int): Pair<Int,Int> {
        val wholeMultiplier = kotlin.math.ceil(earliestPossibleTimestamp / bussId.toDouble())
        return Pair(bussId,(bussId * wholeMultiplier).toInt())
    }

    @Test
    fun `test naive solution`() {
        assertEquals(3417, getNaiveSolution2("17,x,13,19"))
        assertEquals(754018, getNaiveSolution2("67,7,59,61"))
        assertEquals(779210, getNaiveSolution2("67,x,7,59,61"))
        assertEquals(1261476, getNaiveSolution2("67,7,x,59,61"))
        assertEquals(1202161486, getNaiveSolution2("1789,37,47,1889"))
        println(getNaiveSolution2("17,x,x,x,x,x,x,x,x,x,x,37,x,x,x,x,x,409,x,29,x,x,x,x,x,x,x,x,x,x,13,x,x,x,x,x,x,x,x,x,23,x,x,x,x,x,x,x,373,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,19"))

    }

    @Test
    fun `print parsedbuslines`() {
        //eventually using this and the chineese reminder theorem here: https://www.dcode.fr/chinese-remainder
        println(parseBuslines("17,x,x,x,x,x,x,x,x,x,x,37,x,x,x,x,x,409,x,29,x,x,x,x,x,x,x,x,x,x,13,x,x,x,x,x,x,x,x,x,23,x,x,x,x,x,x,x,373,x,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,19"))
    }

    private fun getNaiveSolution(bussLineString: String): Long {
        val bussLines = parseBuslines(bussLineString)
        var foundSolution = false
        var firstBusLineAsLong = (bussLines[0].first).toLong()
        var i = firstBusLineAsLong;
        while (!foundSolution) {
            foundSolution = true
            i += firstBusLineAsLong
            if(i % 100000000L == 0L) println(i)
            for((busline, t) in bussLines) {
                if((i+t) % busline != 0L) {
                    foundSolution = false
                    break
                }
            }
        }
        return i
    }

    private fun getNaiveSolution2(bussLineString: String): Long {
        val bussLines = parseBuslines(bussLineString)
        var foundSolution = false
        var highestBusLineAsLong =  bussLines.maxBy { it.first }!!.first.toLong()
        var highestBusLineT = bussLines.maxBy { it.first }!!.second.toLong()
        var i = 100000000000363L;
        while (!foundSolution) {
            foundSolution = true
            i += highestBusLineAsLong
            if(i % 100000000L == 0L) println(i)
            for((busline, t) in bussLines) {
                if((i-(highestBusLineT-t)) % busline != 0L) {
                    foundSolution = false
                    break
                }


            }

        }
        return i-highestBusLineT

    }

    @Test
    fun `test paring part 2`() {
        assertEquals(listOf(Pair(17,0),Pair(13,2),Pair(19,3)),parseBuslines("17,x,13,19"))
        assertEquals(listOf(Pair(67,0),Pair(7,1),Pair(59,3), Pair(61,4)),parseBuslines("67,7,x,59,61"))
    }

    private fun parseBuslines(bussLines: String): List<Pair<Int, Int>> {
        return bussLines.split(",")
            .mapIndexed{
                index, it -> Pair(it,index)
            }.filter { it.first!="x" }
            .map { Pair(it.first.toInt(),it.second) }
    }


}