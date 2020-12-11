import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Day8Test {


    @Test
    fun `test real input`() {
        println(getAccumulatorBeforeInstructionIsRunTwoTimes(readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day8.txt").toMutableList()))
    }

    @Test
    fun `test real input part 2`() {
        println(getAccumulatorWithCorrectedProgram(readFileAsLinesUsingUseLines("/Users/fredrikfolkesson/git/advent-of-code/src/test/kotlin/input-day8.txt").toMutableList()))
    }


    @Test
    fun `test demo input`() {
        assertEquals(
            5, getAccumulatorBeforeInstructionIsRunTwoTimes(
                ("nop +0\n" +
                        "acc +1\n" +
                        "jmp +4\n" +
                        "acc +3\n" +
                        "jmp -3\n" +
                        "acc -99\n" +
                        "acc +1\n" +
                        "jmp -4\n" +
                        "acc +6").lines().toMutableList()
            )
        )
    }

    @Test
    fun `test demo input_part_two`() {
        assertEquals(
            8, getAccumulatorWithCorrectedProgram(
                ("nop +0\n" +
                        "acc +1\n" +
                        "jmp +4\n" +
                        "acc +3\n" +
                        "jmp -3\n" +
                        "acc -99\n" +
                        "acc +1\n" +
                        "jmp -4\n" +
                        "acc +6").lines().toMutableList()
            )
        )
    }

    private fun getAccumulatorBeforeInstructionIsRunTwoTimes(instructions: MutableList<String>): Int {
        val instructionTriples = instructions.map { toInstructionTriple(it) }.toMutableList()
        return runNextInstructionOrTerminate(instructionTriples, 0, 0)
    }

    private fun getAccumulatorWithCorrectedProgram(instructions: MutableList<String>): Int {
        val originalInstructionTriples = instructions.map { toInstructionTriple(it) }

        return originalInstructionTriples.mapIndexed { index, instructionTriple ->
            val updatedInstructions = originalInstructionTriples.toMutableList()
            val newInstruction =
                if (instructionTriple.first == "jmp") "nop" else if (instructionTriple.first == "nop") "jmp" else "acc"
            updatedInstructions[index] = instructionTriple.copy(first = newInstruction)
            runNextInstructionOrTerminateStrict(updatedInstructions, 0, 0)
        }.filterNotNull().first()

    }


    private fun runNextInstructionOrTerminate(
        instructionTriples: MutableList<Triple<String, Int, Int>>,
        instructionIndex: Int,
        accumulator: Int
    ): Int {
        val instruction = instructionTriples[instructionIndex]
        if (instruction.third == 1) return accumulator
        instructionTriples[instructionIndex] = instruction.copy(third = instruction.third + 1)
        return when (instruction.first) {
            "acc" -> {
                runNextInstructionOrTerminate(
                    instructionTriples,
                    instructionIndex + 1,
                    accumulator + instruction.second
                )
            }
            "jmp" -> {
                runNextInstructionOrTerminate(instructionTriples, instructionIndex + instruction.second, accumulator)
            }
            else -> {
                runNextInstructionOrTerminate(instructionTriples, instructionIndex + 1, accumulator)
            }
        }
    }

    private fun runNextInstructionOrTerminateStrict(
        instructionTriples: MutableList<Triple<String, Int, Int>>,
        instructionIndex: Int,
        accumulator: Int
    ): Int? {
        if (instructionIndex == instructionTriples.size) return accumulator
        val instruction = instructionTriples[instructionIndex]
        if (instruction.third == 1) return null
        instructionTriples[instructionIndex] = instruction.copy(third = instruction.third + 1)
        return when (instruction.first) {
            "acc" -> {
                runNextInstructionOrTerminateStrict(
                    instructionTriples,
                    instructionIndex + 1,
                    accumulator + instruction.second
                )
            }
            "jmp" -> {
                runNextInstructionOrTerminateStrict(
                    instructionTriples,
                    instructionIndex + instruction.second,
                    accumulator
                )
            }
            else -> {
                runNextInstructionOrTerminateStrict(instructionTriples, instructionIndex + 1, accumulator)
            }
        }
    }

    @Test
    fun `test to_instruction_triple`() {
        assertEquals(Triple("nop", 0, 0), toInstructionTriple("nop +0"))
        assertEquals(Triple("acc", 1, 0), toInstructionTriple("acc +1"))
        assertEquals(Triple("jmp", 4, 0), toInstructionTriple("jmp +4"))
        assertEquals(Triple("acc", -99, 0), toInstructionTriple("acc -99"))
    }

    private fun toInstructionTriple(instructionLine: String): Triple<String, Int, Int> {

        return Triple(instructionLine.split(" ")[0], instructionLine.split(" ")[1].toInt(), 0)
    }


}