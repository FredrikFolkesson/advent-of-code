import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day17 {


    @Test
    fun `test getAllNeighbourPositions`() {
        assertEquals(26, findAllNeighbourPositions(Triple(0,0,0)).size)
        assertEquals(26, findAllNeighbourPositions(Triple(0,2,0)).size)
    }

    @Test
    fun `test demo input`() {
        val input =
                ".#.\n" +
                "..#\n" +
                "###"
        val startState = parseInput(input)
        assertEquals(11, bootUp(startState, 1).values.count { it })
        assertEquals(21, bootUp(startState, 2).values.count { it })
        assertEquals(38, bootUp(startState, 3).values.count { it })
        assertEquals(112, bootUp(startState, 6).values.count { it })
    }

    @Test
    fun `test demo input part2`() {
        val input =
            ".#.\n" +
                    "..#\n" +
                    "###"
        val startState = parseInputPart2(input)
        assertEquals(848, bootUpPart2(startState, 6).values.count { it })
    }

    @Test
    fun `test real input`() {
        val input =
                   "###...#.\n" +
                   ".##.####\n" +
                   ".####.##\n" +
                   "###.###.\n" +
                   ".##.####\n" +
                   "#.##..#.\n" +
                   "##.####.\n" +
                   ".####.#."
        val startState = parseInput(input)
        println(bootUp(startState, 6).values.count { it })

    }

    @Test
    fun `test real input part 2`() {
        val input =
            "###...#.\n" +
                    ".##.####\n" +
                    ".####.##\n" +
                    "###.###.\n" +
                    ".##.####\n" +
                    "#.##..#.\n" +
                    "##.####.\n" +
                    ".####.#."
        val startState = parseInputPart2(input)
        println(bootUpPart2(startState, 6).values.count { it })

    }

    private fun bootUp(startState: MutableMap<Triple<Int, Int, Int>, Boolean>, cyclesLeft: Int): MutableMap<Triple<Int, Int, Int>, Boolean> {
        if (cyclesLeft == 0) {
            return startState
        }

        val activeCubes = startState.filter { it.value }

        val endState = activeCubes.map {
            val activeNeighbours = getNumberOfActiveNeighbours(it.key, startState)
            if(activeNeighbours == 2 || activeNeighbours == 3) Pair(it.key, true) else Pair(it.key, false)
        }.toMap().toMutableMap()

        val endStateForNeighboursToActives = activeCubes.keys.fold(mutableSetOf<Triple<Int, Int, Int>>()) { setOfNeighbours, position ->
            setOfNeighbours.addAll(findAllNeighbourPositions(position).filter { it !in activeCubes })
            setOfNeighbours
        }.filter {
            getNumberOfActiveNeighbours(it, startState) == 3
        }.map { it to true }.toMap().toMutableMap()

        endState.putAll(endStateForNeighboursToActives)


        return bootUp(endState ,cyclesLeft-1)

    }

    private fun bootUpPart2(startState: MutableMap<Quadruple<Int, Int, Int,Int>, Boolean>, cyclesLeft: Int): MutableMap<Quadruple<Int, Int, Int,Int>, Boolean> {
        if (cyclesLeft == 0) {
            return startState
        }
        println("Current State: $cyclesLeft")

        val activeCubes = startState.filter { it.value }

        val endState = activeCubes.map {
            val activeNeighbours = getNumberOfActiveNeighbours4d(it.key, startState)
            if(activeNeighbours == 2 || activeNeighbours == 3) Pair(it.key, true) else Pair(it.key, false)
        }.toMap().toMutableMap()

        val endStateForNeighboursToActives = activeCubes.keys.fold(mutableSetOf<Quadruple<Int, Int, Int, Int>>()) { setOfNeighbours, position ->
            setOfNeighbours.addAll(findAllNeighbourPositions4d(position).filter { it !in activeCubes })
            setOfNeighbours
        }.filter {
            getNumberOfActiveNeighbours4d(it, startState) == 3
        }.map { it to true }.toMap().toMutableMap()

        endState.putAll(endStateForNeighboursToActives)


        return bootUpPart2(endState ,cyclesLeft-1)

    }

    @Test
    fun `test getNumberOfActiveNeighbours`() {
        val input =
            ".#.\n" +
            "..#\n"+
            "###"
        val startState = parseInput(input)

        assertEquals(1, getNumberOfActiveNeighbours(Triple(0,0,0), startState))
        assertEquals(1, getNumberOfActiveNeighbours(Triple(1,0,0), startState))
        assertEquals(2, getNumberOfActiveNeighbours(Triple(2,0,0), startState))

        assertEquals(3, getNumberOfActiveNeighbours(Triple(0,1,0), startState))
        assertEquals(5, getNumberOfActiveNeighbours(Triple(1,1,0), startState))
        assertEquals(3, getNumberOfActiveNeighbours(Triple(2,1,0), startState))

        assertEquals(1, getNumberOfActiveNeighbours(Triple(0,2,0), startState))
        assertEquals(3, getNumberOfActiveNeighbours(Triple(1,2,0), startState))
        assertEquals(2, getNumberOfActiveNeighbours(Triple(2,2,0), startState))

    }

    private fun getNumberOfActiveNeighbours(
        position: Triple<Int, Int, Int>,
        startState: MutableMap<Triple<Int, Int, Int>, Boolean>
    ): Int {
        return findAllNeighbourPositions(position).map { startState.getOrDefault(it,false) }.count { it }
    }
    private fun getNumberOfActiveNeighbours4d(
        position: Quadruple<Int, Int, Int, Int>,
        startState: MutableMap<Quadruple<Int, Int, Int, Int>, Boolean>
    ): Int {
        return findAllNeighbourPositions4d(position).map { startState.getOrDefault(it,false) }.count { it }
    }

    @Test
    fun `test parsing`() {
        val input =
            ".#.\n" +
            "..#\n" +
            "###"
        val state = parseInput(input)

        assertEquals(false, state.get(Triple(0,0,0)))
        assertEquals(true, state.get(Triple(1,0,0)))
        assertEquals(false, state.get(Triple(2,0,0)))

        assertEquals(false, state.get(Triple(0,1,0)))
        assertEquals(false, state.get(Triple(1,1,0)))
        assertEquals(true, state.get(Triple(2,1,0)))

        assertEquals(true, state.get(Triple(0,2,0)))
        assertEquals(true, state.get(Triple(1,2,0)))
        assertEquals(true, state.get(Triple(2,2,0)))

    }

    private fun parseInput(input: String): MutableMap<Triple<Int,Int,Int>,Boolean> {
        val state = mutableMapOf<Triple<Int,Int,Int>,Boolean>()
        input.lines().forEachIndexed() { lineIndex, line ->
            line.forEachIndexed { charIndex, char ->
                val position = Triple(charIndex,lineIndex, 0)
                state[position] = char == '#'
            }
        }
        return state
    }

    private fun parseInputPart2(input: String): MutableMap<Quadruple<Int,Int,Int,Int>,Boolean> {
        val state = mutableMapOf<Quadruple<Int,Int,Int,Int>,Boolean>()
            input.lines().forEachIndexed() { lineIndex, line ->
                line.forEachIndexed { charIndex, char ->
                    val position = Quadruple(charIndex,lineIndex, 0, 0)
                    state[position] = char == '#'
                }
            }
        return state

    }


    private fun findAllNeighbourPositions(position: Triple<Int, Int, Int>): Set<Triple<Int, Int, Int>> {
        val (x, y ,z) = position
        val neighbourPositions = mutableSetOf<Triple<Int, Int, Int>>()
        for (i in x-1..x+1) {
            for (j in y-1..y+1) {
                for (k in z-1..z+1) {
                    val neighbourPosition = Triple(i,j,k)
                    if(neighbourPosition != position) neighbourPositions.add(neighbourPosition)
                }
            }
        }
        return neighbourPositions.toSet()

    }

    private fun findAllNeighbourPositions4d(position: Quadruple<Int, Int, Int, Int>): Set<Quadruple<Int, Int, Int, Int>> {
        val (x, y ,z, w) = position
        val neighbourPositions = mutableSetOf<Quadruple<Int, Int, Int, Int>>()
        for (i in x-1..x+1) {
            for (j in y-1..y+1) {
                for (k in z-1..z+1) {
                    for (l in w-1..w+1) {
                        val neighbourPosition = Quadruple(i,j,k,l)
                        if(neighbourPosition != position) neighbourPositions.add(neighbourPosition)
                    }
                }
            }
        }
        return neighbourPositions.toSet()

    }


}