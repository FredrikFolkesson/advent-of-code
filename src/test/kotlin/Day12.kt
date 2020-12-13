import Day12.Action.*
import Day12.Direction.*
import org.junit.jupiter.api.Test
import kotlin.math.abs
import kotlin.test.assertEquals

class Day12 {

    enum class Direction(val degree: Int) {
        NORTH(0), EAST(90),WEST(270),SOUTH(180);

        companion object {
            fun getNewDirection (currentDirection: Direction, changeAmount: Int): Direction {
                val newDegree = (currentDirection.degree + changeAmount) % 360
                return when (newDegree) {
                    0 -> NORTH
                    90 -> EAST
                    180 -> SOUTH
                    270 -> WEST
                    else -> throw java.lang.IllegalArgumentException("Unknown degree: $newDegree")
                }
            }
        }
    }
    enum class Action {
        MOVE_NORTH, MOVE_EAST,MOVE_WEST,MOVE_SOUTH, TURN_LEFT, TURN_RIGHT, FORWARD
    }


    private class Ship(val direction: Direction, val degreesNorth: Int, val degreesEast: Int) {

        fun executeCommand(command: Pair<Action, Int>): Ship {
            val (action, value) = command
            return when (action) {
                MOVE_NORTH -> Ship(direction,degreesNorth + value, degreesEast)
                MOVE_EAST -> Ship(direction,degreesNorth, degreesEast + value)
                MOVE_WEST -> Ship(direction,degreesNorth, degreesEast - value)
                MOVE_SOUTH -> Ship(direction,degreesNorth - value, degreesEast)
                TURN_LEFT -> Ship(Direction.getNewDirection(direction, 3 * value), degreesNorth, degreesEast)
                TURN_RIGHT -> Ship(Direction.getNewDirection(direction, value), degreesNorth, degreesEast)
                FORWARD -> move(value)
            }
        }

        private fun move(value: Int): Ship {
            return when (direction) {
                NORTH -> Ship(direction,degreesNorth + value, degreesEast)
                EAST -> Ship(direction,degreesNorth, degreesEast + value)
                WEST -> Ship(direction,degreesNorth, degreesEast - value)
                SOUTH -> Ship(direction,degreesNorth - value, degreesEast)
            }
        }

        fun getManhattanDistance(): Int {
            return abs(degreesNorth) + abs(degreesEast)
        }

        override fun toString(): String {
            return "Ship(direction=$direction, degreesNorth=$degreesNorth, degreesEast=$degreesEast)"
        }

        fun moveTowardsWaypoint(value: Int, waypoint: Pair<Int, Int>): Pair<Ship, Pair<Int, Int>> {
            val (waypointNorth, waypointEast) = waypoint
            return Pair(Ship(NORTH, degreesNorth + waypointNorth * value,degreesEast + waypointEast * value),waypoint)
        }


    }

    private fun getManhattanDistanceFromStart(direction: Direction, degreesNorth: Int, degreesEast: Int, instructionsAsString: String): Int {

        val instructions = instructionsAsString.lines().map { toCommand(it) }
        println(instructions)

        val endShip = instructions.fold(Ship(direction, degreesNorth, degreesEast)) { currentShip, command ->
            println(currentShip)
            currentShip.executeCommand(command)
        }

        return endShip.getManhattanDistance()
    }

    private fun getManhattanDistanceFromStartPartTwo(direction: Direction, degreesNorth: Int, degreesEast: Int, instructionsAsString: String): Int {

        val instructions = instructionsAsString.lines().map { toCommand(it) }

        val (endShip, endWaypoint) = instructions.fold(Pair(Ship(direction, degreesNorth, degreesEast),Pair(1,10))) { (currentShip, waypoint), command ->
            getNewShipAndWayPointPoition(currentShip, waypoint, command)
        }

        return endShip.getManhattanDistance()
    }

    private fun getNewShipAndWayPointPoition(
        currentShip: Ship,
        waypoint: Pair<Int, Int>,
        command: Pair<Action, Int>
    ): Pair<Ship,Pair<Int,Int>> {

        val (action, value) = command
        return when (action) {
            MOVE_NORTH -> Pair(currentShip,waypoint.copy(first=waypoint.first+value))
            MOVE_EAST -> Pair(currentShip,waypoint.copy(second=waypoint.second+value))
            MOVE_WEST -> Pair(currentShip,waypoint.copy(second=waypoint.second-value))
            MOVE_SOUTH -> Pair(currentShip,waypoint.copy(first=waypoint.first-value))
            TURN_RIGHT -> Pair(currentShip, getNewWaypoint(value, waypoint))
            TURN_LEFT -> Pair(currentShip, getNewWaypoint(value * 3, waypoint))
            FORWARD -> currentShip.moveTowardsWaypoint(value, waypoint)
        }

    }

    private fun getNewWaypoint(degrees: Int, waypoint: Pair<Int, Int>): Pair<Int, Int> {
        val (north, east) = waypoint
        return when (degrees % 360) {
            90 -> Pair(-east,north) //if(east < 0 && north >= 0) Pair(-east,north) else if(east >= 0 && north >= 0) Pair(-east,north) else if(east >= 0 && north < 0) Pair(-east,north)  else Pair(-east,north)
            180 -> Pair(-north,-east)
            270 -> getNewWaypoint(90, Pair(-north,-east))
            else -> throw IllegalArgumentException("unknown degree: $degrees")
        }
    }

    private fun toCommand(commandString: String): Pair<Action, Int> {
        val action = when(commandString[0]) {
            'N' -> MOVE_NORTH
            'E' -> MOVE_EAST
            'S' -> MOVE_SOUTH
            'W' -> MOVE_WEST
            'L' -> TURN_LEFT
            'R' -> TURN_RIGHT
            'F' -> FORWARD
            else -> throw IllegalArgumentException("Unknown char: $commandString[0]")
        }
        val value = commandString.substring(1).toInt()
        return Pair(action,value)


    }

    @Test
    fun `test demo input`() {
        val input =
                "F10\n" +
                "N3\n" +
                "F7\n" +
                "R90\n" +
                "F11"
        assertEquals(286, getManhattanDistanceFromStartPartTwo(EAST,0,0, input))
    }

    @Test
    fun `test real input`() {
        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day12.txt")
        println(getManhattanDistanceFromStartPartTwo(EAST,0,0, input))
    }




    @Test
    fun `test toCommand`() {
        assertEquals(Pair(FORWARD,10), toCommand("F10"))
        assertEquals(Pair(MOVE_NORTH,3), toCommand("N3"))
        assertEquals(Pair(TURN_RIGHT,90), toCommand("R90"))
    }




}