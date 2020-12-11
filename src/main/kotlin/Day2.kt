class Day2 {
    fun findTotalNumberOfCorrectPassword(lines: List<String>): Number = lines.map { line -> PasswordAndValidator(line) }
        .filter { passwordAndValidator -> passwordAndValidator.isValid2() }.size


    class PasswordAndValidator(private val line: String) {
        val password = line.split(" ")[2]
        val minNumberOfTimesOrFirstPos = line.split(" ")[0].split("-")[0].toInt()
        val maxNumberOfTimesOrSecondPos = line.split(" ")[0].split("-")[1].toInt()
        val chosenLetter = line.split(" ")[1][0]

        fun isValid1(): Boolean {
            val count: Int = password.count { it == chosenLetter }
            return count in minNumberOfTimesOrFirstPos..maxNumberOfTimesOrSecondPos
        }

        fun isValid2(): Boolean {
            return (password.get(minNumberOfTimesOrFirstPos - 1) == chosenLetter) xor (password.get(
                maxNumberOfTimesOrSecondPos - 1
            ) == chosenLetter)
        }
    }

}