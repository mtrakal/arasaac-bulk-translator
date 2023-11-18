import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import model.Keyword
import model.Pictogram
import java.io.File

typealias PictogramId = Int

private val jsonOutputSerializer = Json {
    prettyPrint = true
}

fun main(args: Array<String>) {
    // Try adding program arguments via Run/Debug configuration.
    // Learn more about running applications: https://www.jetbrains.com/help/idea/running-applications.html.

    if (args.size != 2) {
        println("Current path: ${System.getProperty("user.dir")}")
        println("Usage: <input.json> <output.json>")
        return
    }

    val inputJson = readFileAndParseToJson(args[0])
    val keysForTranslate = mutableMapOf<PictogramId, List<Keyword>>()

    val outputJson = readFileAndParseToJson(args[1]).onEach { pictogram ->
        pictogram.apply {
            if (keywords.isEmpty()) {
                val keywordsOrigin = inputJson.firstOrNull { pictogram._id == it._id }?.keywords.orEmpty()
                validated = false
                keysForTranslate[_id] = keywordsOrigin
            }
        }
    }

    storeJsonToFile("output.edit.json", jsonOutputSerializer.encodeToString(outputJson))
}

fun readFileAndParseToJson(filename: String): List<Pictogram> {
    readJsonFromFile(filename).let { json ->
        return Json.decodeFromString(json)
    }
}

fun readJsonFromFile(filename: String): String {
    val file = File(filename)
    return file.readText(Charsets.UTF_8)
}

fun storeJsonToFile(filename: String, json: String) {
    val file = File(filename)
    file.writeText(json)
}