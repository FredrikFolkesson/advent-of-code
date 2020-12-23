import com.ginsberg.cirkle.circular
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals


class Day23 {

    @Test
    //Does not work since i changeddestinationCupIndex to use a set as well for speed
    fun `test destinationCupIndex`() {
        var input = "389125467".map { it.toString().toLong() }
        assertEquals(0, destinationCupIndex(input, 3, input.toMutableSet()))
        input = "8931267".map { it.toString().toLong() }
        assertEquals(2, destinationCupIndex(input, 5,input.toMutableSet()))
        input = "8976".map { it.toString().toLong() }
        assertEquals(1, destinationCupIndex(input, 5,input.toMutableSet()))


    }

    private fun destinationCupIndex(
        cups: List<Long>,
        destinationCupLabel: Long,
        cupSet: MutableSet<Long>
    ): Int {
        val cupsSize = cups.size;
        var currentSearchedForLabel = destinationCupLabel
        var destinationCupIndex = -1
        while (destinationCupIndex == -1) {
            if (currentSearchedForLabel in cupSet) {
                destinationCupIndex = cups.indexOf(currentSearchedForLabel)
            }
            currentSearchedForLabel -= 1
            if (currentSearchedForLabel < 1) currentSearchedForLabel = cupsSize.toLong() + 3L
        }
        return destinationCupIndex
    }


    @Test
    //First made using a circular list, then tried to improve it to get it to work for part 2 before giving up and finally using a linked list approach
    fun `part1_semi_improved`() {
        val startCups = "389125467".map { it.toString().toLong() }.toMutableList()
//        var startCups = "716892543".map { it.toString().toLong() }.toMutableList()//.circular()

        val max = startCups.max()!!

        val cups = startCups.toMutableList() //.circular()
        val cupSet = cups.toMutableSet()

        var currentCupIndex = 0
        val cupSize = cups.size
        for (i in 1..100) {
            val currentCup = cups[currentCupIndex]
            println("-- move $i --")

            cups.forEach {
                if(it == currentCup) print("($it) ") else print("$it ")
            }
            println(i)


            val pickUp = mutableListOf<Long>()

            pickUp.add(cups[(currentCupIndex + 1) % cupSize])
            pickUp.add(cups[(currentCupIndex + 2) % cupSize])
            pickUp.add(cups[(currentCupIndex + 3) % cupSize])

            if (currentCupIndex + 1 >= cups.size) cups.removeAt(0) else cups.removeAt(currentCupIndex + 1)
            if (currentCupIndex + 1 >= cups.size) cups.removeAt(0) else cups.removeAt(currentCupIndex + 1)
            if (currentCupIndex + 1 >= cups.size) cups.removeAt(0) else cups.removeAt(currentCupIndex + 1)

            print("pick up: ")
            pickUp.forEach {
                print("$it ")
            }
            println()

            cupSet.removeAll(pickUp)
            val destinationCupIndex = destinationCupIndex(cups, currentCup - 1,  cupSet)
            cupSet.addAll(pickUp)
            println("destination ${cups[destinationCupIndex]}")
            cups.addAll(destinationCupIndex + 1, pickUp)
            currentCupIndex = (cups.indexOf(currentCup) + 1) % cupSize
        }
        println("-- final --")
        println("cups: $cups")

        val labeledOneIndex = cups.indexOf(1)
        val circleCups = cups.circular()
        var string = ""
        for (i in 1..circleCups.size - 1) {
            string = string + circleCups.get(labeledOneIndex + i).toString()
        }
        println(string)


    }


    @Test
    fun part2() {
        val startCups = "716892543".map { it.toString().toLong() }.toMutableList()
        val extraNumbers = (startCups.max()!! + 1)..1000000
        startCups.addAll(extraNumbers.toList())
        val max = startCups.max()!!

        val cupMap = mutableMapOf<Long, Node>()
        val cups = startCups.toLinkedList(cupMap)

        var currentCup = cups
        for (i in 1..10000000) {
            val pickUp1 = currentCup.next
            val pickUp2 = currentCup.next.next
            val pickUp3 = currentCup.next.next.next
            val toMove = listOf(pickUp1, pickUp2, pickUp3)


            val next = pickUp3.next
            currentCup.next = next


            var searchLabel = if (currentCup.data - 1 == 0L) max else currentCup.data - 1
            var dest = cupMap.get(searchLabel)!!
            while (dest in toMove) {
                searchLabel = if (searchLabel - 1 == 0L) max else searchLabel - 1
                dest = cupMap.get(searchLabel)!!
            }

            val after = dest.next
            dest.next = toMove[0]
            toMove[2].next = after

            currentCup = next


        }
        println(cupMap.get(1L)!!.next.data * cupMap.get(1L)!!.next.next.data)




    }

    //Found while looking online
    private fun List<Long>.toLinkedList(cupMap: MutableMap<Long, Node>): Node {
        val head = Node(this[0])
        var prev = head
        cupMap.put(this[0], head)
        for (i in 1 until size) {
            val next = Node(this[i])
            cupMap.put(this[i], next)
            prev.next = next
            prev = next
        }

        prev.next = head
        return head
    }

    class Node(val data: Long) : Comparable<Node> {
        lateinit var next: Node

        override fun compareTo(other: Node): Int {
            return data.compareTo(other.data)
        }

        override fun toString(): String {
            return "Data $data"
        }
    }


}


