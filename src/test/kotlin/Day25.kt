import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class Day25 {

    @Test
    fun `test transformation`() {
        assertEquals(8, findLoopSize(7, 5764801))
        assertEquals(11, findLoopSize(7, 17807724))
    }

    @Test
    fun `test demo input`() {
        val loopSize1 = findLoopSize(7, 5764801)
        val encryptionKey = transformSubjectNumber(17807724, loopSize1)
        println("encryptionkey $encryptionKey")
    }

    private fun transformSubjectNumber(subjectNumber: Long, loopSize: Long): Long {
//        val transformed = (0 until loopSize).fold(1L) { acc, it ->
//            val transformedNumber = acc * subjectNumber
//            transformedNumber % 20201227
//        }
//        return transformed

        var transformedNumber = 1L
        repeat(loopSize.toInt()) {
            transformedNumber *= subjectNumber
            transformedNumber %= 20201227
        }
        return transformedNumber
    }

    @Test
    fun `part1`() {
        val loopSize = findLoopSize(7, 3248366)
        val encryptionKey = transformSubjectNumber(4738476, loopSize)
        println("encryptionkey $encryptionKey")
    }


    private fun findLoopSize(subjectNumber: Long, publicKey: Long): Long {
        var transformedNumber = 1L
        var loop = 0L
        while (transformedNumber != publicKey) {
            transformedNumber *= subjectNumber
            transformedNumber %= 20201227
            loop += 1
        }
        return loop
    }
}



