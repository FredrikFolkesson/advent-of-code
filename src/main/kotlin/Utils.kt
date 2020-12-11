import java.io.File

public fun readFileAsLinesUsingUseLines(fileName: String): List<String> = File(fileName).useLines { it.toList() }
public fun readFileAsString(fileName: String): String = File(fileName).readText()