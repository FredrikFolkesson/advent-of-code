import Day24.Direction.*
import Day24.Tile.*
import org.junit.jupiter.api.Test
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals


class Day24 {

    enum class Tile {
        WHITE, BLACK;

        fun flip(): Day24.Tile {
            return if (this == BLACK) WHITE else BLACK
        }
    }

    enum class Direction {
        EAST,
        SOUTH_EAST,
        SOUTH_WEST,
        WEST,
        NORTH_WEST,
        NORTH_EAST
    }

    @Test
    fun `part 1`() {
        val demoInput = "sesenwnenenewseeswwswswwnenewsewsw\n" +
                "neeenesenwnwwswnenewnwwsewnenwseswesw\n" +
                "seswneswswsenwwnwse\n" +
                "nwnwneseeswswnenewneswwnewseswneseene\n" +
                "swweswneswnenwsewnwneneseenw\n" +
                "eesenwseswswnenwswnwnwsewwnwsene\n" +
                "sewnenenenesenwsewnenwwwse\n" +
                "wenwwweseeeweswwwnwwe\n" +
                "wsweesenenewnwwnwsenewsenwwsesesenwne\n" +
                "neeswseenwwswnwswswnw\n" +
                "nenwswwsewswnenenewsenwsenwnesesenew\n" +
                "enewnwewneswsewnwswenweswnenwsenwsw\n" +
                "sweneswneswneneenwnewenewwneswswnese\n" +
                "swwesenesewenwneswnwwneseswwne\n" +
                "enesenwswwswneneswsenwnewswseenwsese\n" +
                "wnwnesenesenenwwnenwsewesewsesesew\n" +
                "nenewswnwewswnenesenwnesewesw\n" +
                "eneswnwswnwsenenwnwnwwseeswneewsenese\n" +
                "neswnwewnwnwseenwseesewsenwsweewe\n" +
                "wseweeenwnesenwwwswnew"

        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day24.txt")


        val blackTileCount = getTileMap(input)
            .values
            .count {
                it == BLACK
            }
        println(blackTileCount)


    }

    @Test
    fun `part 2`() {
        val demoInput = "sesenwnenenewseeswwswswwnenewsewsw\n" +
                "neeenesenwnwwswnenewnwwsewnenwseswesw\n" +
                "seswneswswsenwwnwse\n" +
                "nwnwneseeswswnenewneswwnewseswneseene\n" +
                "swweswneswnenwsewnwneneseenw\n" +
                "eesenwseswswnenwswnwnwsewwnwsene\n" +
                "sewnenenenesenwsewnenwwwse\n" +
                "wenwwweseeeweswwwnwwe\n" +
                "wsweesenenewnwwnwsenewsenwwsesesenwne\n" +
                "neeswseenwwswnwswswnw\n" +
                "nenwswwsewswnenenewsenwsenwnesesenew\n" +
                "enewnwewneswsewnwswenweswnenwsenwsw\n" +
                "sweneswneswneneenwnewenewwneswswnese\n" +
                "swwesenesewenwneswnwwneseswwne\n" +
                "enesenwswwswneneswsenwnewswseenwsese\n" +
                "wnwnesenesenenwwnenwsewesewsesesew\n" +
                "nenewswnwewswnenesenwnesewesw\n" +
                "eneswnwswnwsenenwnwnwwseeswneewsenese\n" +
                "neswnwewnwnwseenwseesewsenwsweewe\n" +
                "wseweeenwnesenwwwswnew"

        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day24.txt")


        val tileMap = getTileMap(input)
        repeat(100) {


            val currentBlackTiles = tileMap.filter { it.value == BLACK }

            val neighboursToBlackTiles = currentBlackTiles.flatMap {
                getNeighbourPositions(it.key)
            }.toSet().filter { it !in currentBlackTiles.keys }


            //Här borde alla som var svarta finns med sin nya status
            val updatedBlackTiles = currentBlackTiles.map { blackTile ->
                val numberOfBlackNeighbours = getNeighboursColors(blackTile.key, tileMap).count { it == BLACK }
                if (numberOfBlackNeighbours == 0 || numberOfBlackNeighbours > 2) {
                    blackTile.key to WHITE
                } else {
                    blackTile.key to BLACK
                }
            }
            val updatedNeighbourTiles = neighboursToBlackTiles.map {
                val numberOfBlackNeighbours = getNeighboursColors(it, tileMap).count { it == BLACK }
                if (numberOfBlackNeighbours == 2) {
                    it to BLACK
                } else {
                    it to WHITE
                }
            }

            tileMap.putAll(updatedBlackTiles)
            tileMap.putAll(updatedNeighbourTiles)

            println("Day $it: ${tileMap.values.count { it == BLACK }}")
        }


    }

    private fun getTileMap(input: String) = input
        .lines()
        .map { parseTileDirections(it) }
        .fold(mutableMapOf<Pair<Int, Int>, Tile>()) { mapOfTiles, tileDirections ->
            val endPos = tileDirections.fold(Pair(0, 0)) { currentPos, direction ->
                currentPos + directionToChange(direction)
            }
            mapOfTiles.put(endPos, mapOfTiles.getOrDefault(endPos, WHITE).flip())
            mapOfTiles
        }


    @Test
    fun `test get neighbours`() {
        assertEquals(
            listOf(WHITE, WHITE, WHITE, WHITE, WHITE, WHITE),
            getNeighboursColors(Pair(0, 0), mapOf<Pair<Int, Int>, Tile>())
        )
        assertEquals(
            listOf(BLACK, WHITE, WHITE, WHITE, WHITE, WHITE),
            getNeighboursColors(Pair(0, 0), mapOf<Pair<Int, Int>, Tile>(Pair(0, 2) to BLACK))
        )
        assertEquals(
            listOf(BLACK, WHITE, BLACK, WHITE, WHITE, WHITE),
            getNeighboursColors(Pair(0, 0), mapOf<Pair<Int, Int>, Tile>(Pair(0, 2) to BLACK, Pair(-2, -1) to BLACK))
        )
    }


    private fun getNeighbourPositions(tile: Pair<Int, Int>): List<Pair<Int, Int>> {
        val (n, e) = tile
        return listOf(
            Pair(n, e + 2),
            Pair(n - 2, e + 1),
            Pair(n - 2, e - 1),
            Pair(n, e - 2),
            Pair(n + 2, e - 1),
            Pair(n + 2, e + 1),
        )
    }

    private fun getNeighboursColors(tile: Pair<Int, Int>, tileMap: Map<Pair<Int, Int>, Tile>): List<Tile> {
        return listOf(
            tileMap.getOrDefault(Pair(tile.first, tile.second + 2), WHITE),
            tileMap.getOrDefault(Pair(tile.first - 2, tile.second + 1), WHITE),
            tileMap.getOrDefault(Pair(tile.first - 2, tile.second - 1), WHITE),
            tileMap.getOrDefault(Pair(tile.first, tile.second - 2), WHITE),
            tileMap.getOrDefault(Pair(tile.first + 2, tile.second - 1), WHITE),
            tileMap.getOrDefault(Pair(tile.first + 2, tile.second + 1), WHITE)
        )

    }


    private fun directionToChange(direction: Direction): Pair<Int, Int> {
        return when (direction) {
            EAST -> Pair(0, 2)
            SOUTH_EAST -> Pair(-2, 1)
            SOUTH_WEST -> Pair(-2, -1)
            WEST -> Pair(0, -2)
            NORTH_WEST -> Pair(2, -1)
            NORTH_EAST -> Pair(2, 1)
        }
    }


    @Test
    fun `test flipping`() {
        assertEquals(WHITE, BLACK.flip())
        assertEquals(BLACK, WHITE.flip())
        assertEquals(WHITE, WHITE.flip().flip())
        assertEquals(BLACK, WHITE.flip().flip().flip())
    }

    @Test
    fun `test pair addition`() {
        assertEquals(Pair(3, 7), Pair(1, 4) + Pair(2, 3))
        assertEquals(Pair(0, 10), Pair(5, 5) + Pair(-5, 5))
    }

    @Test
    fun `test direction parsing`() {
        assertEquals(listOf(SOUTH_EAST), parseTileDirections("se"))
        assertEquals(
            listOf(
                SOUTH_EAST,
                SOUTH_EAST,
                NORTH_WEST,
                NORTH_EAST,
                NORTH_EAST,
                NORTH_EAST,
                WEST,
                SOUTH_EAST,
                EAST,
                SOUTH_WEST,
                WEST,
                SOUTH_WEST,
                SOUTH_WEST,
                WEST,
                NORTH_EAST,
                NORTH_EAST,
                WEST,
                SOUTH_EAST,
                WEST,
                SOUTH_WEST
            ), parseTileDirections("sesenwnenenewseeswwswswwnenewsewsw")
        )
    }

    private fun parseTileDirections(line: String): List<Direction> {
        var restOfDirections = line
        val directions = mutableListOf<Direction>()
        while (restOfDirections.isNotEmpty()) {
            val directionString =
                if (restOfDirections.length > 1) restOfDirections.substring(0, 2) else restOfDirections.first()
                    .toString()
            when (directionString) {
                "se" -> {
                    directions.add(SOUTH_EAST)
                    restOfDirections = restOfDirections.substring(2)
                }
                "sw" -> {
                    directions.add(SOUTH_WEST)
                    restOfDirections = restOfDirections.substring(2)
                }
                "nw" -> {
                    directions.add(NORTH_WEST)
                    restOfDirections = restOfDirections.substring(2)
                }
                "ne" -> {
                    directions.add(NORTH_EAST)
                    restOfDirections = restOfDirections.substring(2)
                }
                else -> {
                    restOfDirections = when {
                        directionString.first() == 'e' -> {
                            directions.add(EAST)
                            restOfDirections.substring(1)
                        }
                        directionString.first() == 'w' -> {
                            directions.add(WEST)
                            restOfDirections.substring(1)
                        }
                        else -> {
                            throw IllegalArgumentException("Unkown direction $directionString")
                        }
                    }
                }
            }
        }
        return directions

    }


}

private operator fun Pair<Int, Int>.plus(addition: Pair<Int, Int>): Pair<Int, Int> {
    return Pair(this.first + addition.first, this.second + addition.second)
}



