package files

import arasaac.model.Pictogram
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

class FileWriterJsonImpl(private val jsonSerializer: Json) : FileWriter<List<Pictogram>> {
    override val type = FileWriter.Type.JSON

    private val List<Pictogram>.toJson
        get() = jsonSerializer.encodeToString(this)

    override fun write(filename: String, content: List<Pictogram>) {
        val file = File(filename)
        file.writeText(content.toJson)
    }

    override fun read(filename: String): List<Pictogram> {
        readJsonFromFile(filename).let { json ->
            return Json.decodeFromString(json)
        }
    }

    private fun readJsonFromFile(filename: String): String {
        val file = File(filename)
        return file.readText(Charsets.UTF_8)
    }
}
