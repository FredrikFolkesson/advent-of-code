import org.junit.jupiter.api.Test
import kotlin.math.absoluteValue
import kotlin.test.assertEquals


class Day20 {

    @Test
    fun `test parseTiles`() {
        assertEquals(mapOf<String, Quadruple<String,String,String,String>>("Tile 2311:" to Quadruple("..##.#..#.","...#.##..#", "..###..###",".#####..#.")), parseTiles("Tile 2311:\n" +
                "..##.#..#.\n" +
                "##..#.....\n" +
                "#...##..#.\n" +
                "####.#...#\n" +
                "##.##.###.\n" +
                "##...#.###\n" +
                ".#.#.#..##\n" +
                "..#....#..\n" +
                "###...#.#.\n" +
                "..###..###"))
    }



    @Test
    fun findMatches() {
        val demoInput = "Tile 2311:\n" +
                "..##.#..#.\n" +
                "##..#.....\n" +
                "#...##..#.\n" +
                "####.#...#\n" +
                "##.##.###.\n" +
                "##...#.###\n" +
                ".#.#.#..##\n" +
                "..#....#..\n" +
                "###...#.#.\n" +
                "..###..###\n" +
                "\n" +
                "Tile 1951:\n" +
                "#.##...##.\n" +
                "#.####...#\n" +
                ".....#..##\n" +
                "#...######\n" +
                ".##.#....#\n" +
                ".###.#####\n" +
                "###.##.##.\n" +
                ".###....#.\n" +
                "..#.#..#.#\n" +
                "#...##.#..\n" +
                "\n" +
                "Tile 1171:\n" +
                "####...##.\n" +
                "#..##.#..#\n" +
                "##.#..#.#.\n" +
                ".###.####.\n" +
                "..###.####\n" +
                ".##....##.\n" +
                ".#...####.\n" +
                "#.##.####.\n" +
                "####..#...\n" +
                ".....##...\n" +
                "\n" +
                "Tile 1427:\n" +
                "###.##.#..\n" +
                ".#..#.##..\n" +
                ".#.##.#..#\n" +
                "#.#.#.##.#\n" +
                "....#...##\n" +
                "...##..##.\n" +
                "...#.#####\n" +
                ".#.####.#.\n" +
                "..#..###.#\n" +
                "..##.#..#.\n" +
                "\n" +
                "Tile 1489:\n" +
                "##.#.#....\n" +
                "..##...#..\n" +
                ".##..##...\n" +
                "..#...#...\n" +
                "#####...#.\n" +
                "#..#.#.#.#\n" +
                "...#.#.#..\n" +
                "##.#...##.\n" +
                "..##.##.##\n" +
                "###.##.#..\n" +
                "\n" +
                "Tile 2473:\n" +
                "#....####.\n" +
                "#..#.##...\n" +
                "#.##..#...\n" +
                "######.#.#\n" +
                ".#...#.#.#\n" +
                ".#########\n" +
                ".###.#..#.\n" +
                "########.#\n" +
                "##...##.#.\n" +
                "..###.#.#.\n" +
                "\n" +
                "Tile 2971:\n" +
                "..#.#....#\n" +
                "#...###...\n" +
                "#.#.###...\n" +
                "##.##..#..\n" +
                ".#####..##\n" +
                ".#..####.#\n" +
                "#..#.#..#.\n" +
                "..####.###\n" +
                "..#.#.###.\n" +
                "...#.#.#.#\n" +
                "\n" +
                "Tile 2729:\n" +
                "...#.#.#.#\n" +
                "####.#....\n" +
                "..#.#.....\n" +
                "....#..#.#\n" +
                ".##..##.#.\n" +
                ".#.####...\n" +
                "####.#.#..\n" +
                "##.####...\n" +
                "##..#.##..\n" +
                "#.##...##.\n" +
                "\n" +
                "Tile 3079:\n" +
                "#.#.#####.\n" +
                ".#..######\n" +
                "..#.......\n" +
                "######....\n" +
                "####.#..#.\n" +
                ".#...#.##.\n" +
                "#.#####.##\n" +
                "..#.###...\n" +
                "..#.......\n" +
                "..#.###..."

        val input = readFileAsString("/Users/fredrikfolkesson/git/advent-of-code/inputs/input-day20.txt")

        val tiles = parseTiles(demoInput)
        val wholeTiles = parseTilesWhole(demoInput)
//        println(wholeTiles)
//        return

        val addedLines = mutableSetOf<String>()
        val lineToTile = mutableMapOf<String,String>()
        val tileToNumberOfTimesFound = mutableMapOf<String,Int>()
        val tileToNeighbours = mutableMapOf<String,List<String>>()
        val tileToNeighboursPos = mutableMapOf<String,List<Pair<String,String>>>()

        tiles.forEach{

            if(!addedLines.add(it.value.first)) {
                tileToNumberOfTimesFound.put(it.key, tileToNumberOfTimesFound.getOrDefault(it.key, 0) + 1)
                tileToNumberOfTimesFound.put(lineToTile.get(it.value.first)!!, tileToNumberOfTimesFound.getOrDefault(lineToTile.get(it.value.first)!!, 0) + 1)

                tileToNeighbours.put(it.key, tileToNeighbours.getOrDefault(it.key, listOf()) + listOf(lineToTile.get(it.value.first)!!))
                tileToNeighbours.put(lineToTile.get(it.value.first)!!, tileToNeighbours.getOrDefault(lineToTile.get(it.value.first)!!, listOf()) + listOf(it.key))


                val otherTileName = lineToTile.get(it.value.first)!!
                val otherTimePos = tiles.get(otherTileName)!!.position(it.value.first)
                tileToNeighboursPos.put(it.key, tileToNeighboursPos.getOrDefault(it.key, listOf()) + listOf(Pair(otherTileName,otherTimePos)))
                tileToNeighboursPos.put(lineToTile.get(it.value.first)!!, tileToNeighboursPos.getOrDefault(lineToTile.get(it.value.first)!!, listOf()) + listOf(Pair(it.key,it.value.position(it.value.first))))

//                println("Found a first line already found: ${it.value.first} in ${it.key}  already seen in ${lineToTile.get(it.value.first)}")
            } else lineToTile.put(it.value.first, it.key)
            if(!addedLines.add(it.value.first.reversed())) else lineToTile.put(it.value.first.reversed(), it.key)
            if(!addedLines.add(it.value.second)) {
                tileToNumberOfTimesFound.put(it.key, tileToNumberOfTimesFound.getOrDefault(it.key, 0) + 1)
                tileToNumberOfTimesFound.put(lineToTile.get(it.value.second)!!, tileToNumberOfTimesFound.getOrDefault(lineToTile.get(it.value.second)!!, 0) + 1)
//                println("Found a second line already found: ${it.value.second} in ${it.key}  already seen in ${lineToTile.get(it.value.second)}")
                tileToNeighbours.put(it.key, tileToNeighbours.getOrDefault(it.key, listOf()) + listOf(lineToTile.get(it.value.second)!!))
                tileToNeighbours.put(lineToTile.get(it.value.second)!!, tileToNeighbours.getOrDefault(lineToTile.get(it.value.second)!!, listOf()) + listOf(it.key))

                val otherTileName = lineToTile.get(it.value.second)!!
                val otherTimePos = tiles.get(otherTileName)!!.position(it.value.second)
                tileToNeighboursPos.put(it.key, tileToNeighboursPos.getOrDefault(it.key, listOf()) + listOf(Pair(otherTileName,otherTimePos)))
                tileToNeighboursPos.put(lineToTile.get(it.value.second)!!, tileToNeighboursPos.getOrDefault(lineToTile.get(it.value.second)!!, listOf()) + listOf(Pair(it.key,it.value.position(it.value.second))))

            } else lineToTile.put(it.value.second, it.key)
            if(!addedLines.add(it.value.second.reversed())) else lineToTile.put(it.value.second.reversed(), it.key)
            if(!addedLines.add(it.value.third)) {
                tileToNumberOfTimesFound.put(it.key, tileToNumberOfTimesFound.getOrDefault(it.key, 0) + 1)
                tileToNumberOfTimesFound.put(lineToTile.get(it.value.third)!!, tileToNumberOfTimesFound.getOrDefault(lineToTile.get(it.value.third)!!, 0) + 1)
//                println("Found a third line already found: ${it.value.third} in ${it.key} already seen in ${lineToTile.get(it.value.third)}")
                tileToNeighbours.put(it.key, tileToNeighbours.getOrDefault(it.key, listOf()) + listOf(lineToTile.get(it.value.third)!!))
                tileToNeighbours.put(lineToTile.get(it.value.third)!!, tileToNeighbours.getOrDefault(lineToTile.get(it.value.third)!!, listOf()) + listOf(it.key))

                val otherTileName = lineToTile.get(it.value.third)!!
                val otherTimePos = tiles.get(otherTileName)!!.position(it.value.third)
                tileToNeighboursPos.put(it.key, tileToNeighboursPos.getOrDefault(it.key, listOf()) + listOf(Pair(otherTileName,otherTimePos)))
                tileToNeighboursPos.put(lineToTile.get(it.value.third)!!, tileToNeighboursPos.getOrDefault(lineToTile.get(it.value.third)!!, listOf()) + listOf(Pair(it.key,it.value.position(it.value.third))))

            } else lineToTile.put(it.value.third, it.key)
            if(!addedLines.add(it.value.third.reversed()))  else lineToTile.put(it.value.third.reversed(), it.key)
            if(!addedLines.add(it.value.fourth)) {
                tileToNumberOfTimesFound.put(it.key, tileToNumberOfTimesFound.getOrDefault(it.key, 0) + 1)
                tileToNumberOfTimesFound.put(lineToTile.get(it.value.fourth)!!, tileToNumberOfTimesFound.getOrDefault(lineToTile.get(it.value.fourth)!!, 0) + 1)
//                println("Found a fourth line already found: ${it.value.fourth} in ${it.key} already seen in ${lineToTile.get(it.value.fourth)}")
                tileToNeighbours.put(it.key, tileToNeighbours.getOrDefault(it.key, listOf()) + listOf(lineToTile.get(it.value.fourth)!!))
                tileToNeighbours.put(lineToTile.get(it.value.fourth)!!, tileToNeighbours.getOrDefault(lineToTile.get(it.value.fourth)!!, listOf()) + listOf(it.key))

                val otherTileName = lineToTile.get(it.value.fourth)!!
                val otherTimePos = tiles.get(otherTileName)!!.position(it.value.fourth)
                tileToNeighboursPos.put(it.key, tileToNeighboursPos.getOrDefault(it.key, listOf()) + listOf(Pair(otherTileName,otherTimePos)))
                tileToNeighboursPos.put(lineToTile.get(it.value.fourth)!!, tileToNeighboursPos.getOrDefault(lineToTile.get(it.value.fourth)!!, listOf()) + listOf(Pair(it.key,it.value.position(it.value.fourth))))


            } else lineToTile.put(it.value.fourth, it.key)
            if(!addedLines.add(it.value.fourth.reversed()))  else lineToTile.put(it.value.fourth.reversed(), it.key)
        }

        val matrix = mutableMapOf<Pair<Int,Int>,String>()
        var sortedEntries = tileToNeighboursPos.entries.sortedBy { it.value.size }
        val tileToPosition = mutableMapOf<String,Pair<Int,Int>>()
        val tileToDirection = mutableMapOf<String,String>()

        tileToPosition.put(sortedEntries.first().key,Pair(0,0))

        while (sortedEntries.isNotEmpty()) {
//            println(sortedEntries)
            //Just to get a value
            var selectedEntry = sortedEntries.first()

            for(entry in sortedEntries) {
                if(tileToPosition.contains(entry.key)) {
                    println("will add and remove" + entry.key)
                    selectedEntry = entry
                    break
                }
            }

            var temp = sortedEntries.toMutableSet()
            temp.remove(selectedEntry)
            sortedEntries = temp.toList()

            println(selectedEntry.key)
            val (x, y) = tileToPosition.get(selectedEntry.key)!!
            println("x $x, y $y")

            for(neighbour in selectedEntry.value) {
                println("Neighbour $neighbour")
                val neighbourX = if(neighbour.second.contains("second")) x+10 else if(neighbour.second.contains("fourth")) x-10 else x
                val neighbourY = if(neighbour.second.contains("first")) y-10 else if(neighbour.second.contains("third")) y+10 else y
                tileToPosition.put(neighbour.first, neighbourX to neighbourY)
            }
            // Tile 2473:=[(Tile 1427:, second), (Tile 1171:, first), (Tile 3079:, third)]
            println("Matrix before: "+matrix.size)
            val wholeTile = wholeTiles.get(selectedEntry.key)
            for((yIndex, i) in (y..y+9).withIndex()){
                for((xIndex, j) in (x..x+9).withIndex()){
                    matrix.put(Pair(j,i), wholeTile!![yIndex][xIndex].toString())
                }
            }
            println("Matrix after: "+matrix.size)
            println("Neighbours after $tileToPosition")







        }
        println(tileToPosition.size)
        println(matrix)


        val xMin = matrix.keys.minOf { it.first }
        val yMin = matrix.keys.minOf { it.second }

        var sortedMatrix = matrix.mapKeys { Pair(it.key.first+(xMin.absoluteValue), it.key.second+(yMin.absoluteValue)) }
//        println(sortedMatrix)
        sortedMatrix = sortedMatrix.toSortedMap(compareBy ({ it.second }, { it.first }))
        println(sortedMatrix.size)
        var counter = 0
        for(entry in sortedMatrix.entries) {
            print(entry.value)
            counter++
            if (counter %30 == 0) println()
        }


//        println(tileToNumberOfTimesFound)
//        val ids= tileToNumberOfTimesFound
//            .filter { it.value == 2 }
//            .map { it.key }
//            .map { it.substringAfter(" ").replace(":","").toLong() }
//
//
//        println(ids.fold(1L) { acc, id ->
//            acc * id
//        })
//
//        println()
//        println(tileToNeighbours)
//        println()
//        println(tileToNeighboursPos)

    }



    private fun parseTiles(input: String): Map<String, Quadruple<String, String, String, String>> {
        return input.split("\n\n").map {

            val key = it.lines().first()
            key to parseTile(it.lines().drop(1))
        }.toMap()
    }

    private fun parseTile(tileLines: List<String>): Quadruple<String, String, String, String> {
        val line1 = tileLines.first()
        val line3 = tileLines.last()
        var line2 = ""
        var line4 = ""

        tileLines.forEach {
            line2 += it.last()
            line4 += it.first()
        }
        return Quadruple(line1,line2,line3,line4)

    }


    private fun parseTilesWhole(input: String): Map<String, List<String>> {

        return input.split("\n\n").map {

            val key = it.lines().first()
            key to it.lines().drop(1)
        }.toMap()
    }


    fun Quadruple<String,String,String,String>.position(value: String): String = when (value) {
        this.first -> "first"
        this.second -> "second"
        this.third -> "third"
        this.fourth -> "fourth"
        this.first.reversed() -> "first reversed"
        this.second.reversed() -> "second reversed"
        this.third.reversed() -> "third reversed"
        this.fourth.reversed() -> "fourth reversed"
        else -> "UNKNOWN"
    }


}


