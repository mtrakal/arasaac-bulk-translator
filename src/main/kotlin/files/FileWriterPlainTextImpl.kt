package files

import java.io.File

class FileWriterPlainTextImpl : FileWriter<List<String>> {
    override val type = FileWriter.Type.PLAIN_TEXT

    override fun write(filename: String, content: List<String>) = File(filename).writeText(content.joinToString("\n"))

    override fun read(filename: String): List<String> = File(filename).readText().split("\n")
}
