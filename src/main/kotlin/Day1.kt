class Day1 {

    fun getEntriesThatSumTo2020(lines: List<String>): Triple<Int, Int, Int> {
        val linesAsNumbers: List<Int> = lines.map { line -> line.toInt() }
        for (i in 0 until linesAsNumbers.size - 2) {
            for (j in i + 1 until linesAsNumbers.size - 1) {
                for (k in i + 2 until linesAsNumbers.size) {
                    if (linesAsNumbers[i] + linesAsNumbers[j] + linesAsNumbers[k] == 2020) {
                        return Triple(linesAsNumbers[i], linesAsNumbers[j], linesAsNumbers[k])
                    }
                }
            }
        }
        throw Exception()
    }
}