import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day14 {



    @Test
    fun `part1`() {
        val input = readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day14.txt")
        println(getSumOfMemory(input))
    }

    private fun getSumOfMemory(instructions: List<String>): Long {

        return instructions.fold(Pair("", mapOf<Long,Long>())) { acc, line ->
            if(line.contains("mask")) {
                val newMask = line.split(" = ")[1]
                Pair(newMask,acc.second)
            } else {
                val (memoryAddress, value) = toMemoryAddressAndValuePair(line)
                Pair(acc.first, acc.second + mapOf(memoryAddress to numberAfterMasking(value.toInt(), acc.first)))
            }
        }.second.values.sum().toLong()
    }


    @Test
    fun `test mem-line parsing`() {
        assertEquals(Pair(24821,349), toMemoryAddressAndValuePair("mem[24821] = 349"))
        assertEquals(Pair(34917,13006), toMemoryAddressAndValuePair("mem[34917] = 13006"))
        assertEquals(Pair(53529,733), toMemoryAddressAndValuePair("mem[53529] = 733"))
        assertEquals(Pair(50289,245744), toMemoryAddressAndValuePair("mem[50289] = 245744"))
        assertEquals(Pair(23082,6267), toMemoryAddressAndValuePair("mem[23082] = 6267"))
    }

    private fun toMemoryAddressAndValuePair(input: String): Pair<Long, Long> {
        val value = input.split(" = ")[1].toLong()
        val memoryAddress = input.split("] = ")[0].split("mem[")[1].toLong()
        return Pair(memoryAddress,value)
    }

    @Test
    fun `test masking of memoryAdress`() {
        assertEquals(listOf(26L,27L,58L,59L),addressesAfterMasking(42, "000000000000000000000000000000X1001X"))
        assertEquals(listOf(16L,17L,18L,19L,24L,25L,26L,27L),addressesAfterMasking(26, "00000000000000000000000000000000X0XX"))
    }

    @Test
    fun `demo part 2`() {
        val instructions =
                    "mask = 000000000000000000000000000000X1001X\n" +
                    "mem[42] = 100\n" +
                    "mask = 00000000000000000000000000000000X0XX\n" +
                    "mem[26] = 1"
        assertEquals(208, getSumOfMemoryPart2(instructions.lines()))
    }

    @Test
    fun `strict part 2`() {
        println(getSumOfMemoryPart2(readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day14.txt")))
    }

    private fun getSumOfMemoryPart2(instructions: List<String>): Long {
        return instructions.fold(Pair("", mapOf<Long,Long>())) { acc, line ->
            if(line.contains("mask")) {
                val newMask = line.split(" = ")[1]
                Pair(newMask,acc.second)
            } else {
                val (memoryAddress, value) = toMemoryAddressAndValuePair(line)
                Pair(acc.first, acc.second + addressesAfterMasking(memoryAddress.toInt(), acc.first).map { it to value }.toMap())
            }
        }.second.values.sum()
    }

    private fun addressesAfterMasking(memoryAdress: Int, mask: String): List<Long> {
        val memoryAdressBinaryString = Integer.toBinaryString(memoryAdress).padStart(36,'0')
        val memoryAdressBinaryStringWithOnesAndXes = mask.mapIndexed{index, it ->
            if(it =='0') memoryAdressBinaryString[index] else it
        }.joinToString("").padStart(36,'0')

        return getMemoryListsWithoutX(memoryAdressBinaryStringWithOnesAndXes)
    }

    private fun getMemoryListsWithoutX(memoryMask: String): List<Long> {
        if(!memoryMask.contains("X")) return listOf(memoryMask.toLong(2))
        else {
            return getMemoryListsWithoutX(memoryMask.replaceFirst("X", "0")) + (getMemoryListsWithoutX(memoryMask.replaceFirst("X", "1")))
        }
    }

    @Test
    fun `test masking of number`() {
        assertEquals(73,numberAfterMasking(11, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"))
        assertEquals(101,numberAfterMasking(101, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"))
        assertEquals(64,numberAfterMasking(0, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXX1XXXX0X"))
    }


    private fun numberAfterMasking(number: Int, mask: String): Long {
        val numberAsBinaryString = Integer.toBinaryString(number).padStart(36,'0')
        val numberAsLong = number.toLong()
        val orMaskString = mask.mapIndexed{index, it ->
            if(it =='1') it else numberAsBinaryString[index]
        }.joinToString("").padStart(36,'0')
        val orMaskInt = orMaskString.toLong(2)


        val andMaskString = mask.mapIndexed{index, it ->
            if(it =='0') it else orMaskString[index]
        }.joinToString("").padStart(36,'0')
        val andMaskInt = andMaskString.toLong(2)

        return numberAsLong or orMaskInt and andMaskInt
    }

    private fun to36BitStringFormat(number: Int): String {
        val i = 5 and 2

        return "1100"
    }


}