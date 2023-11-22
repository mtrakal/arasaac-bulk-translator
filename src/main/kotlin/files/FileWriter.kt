package files

interface FileWriter {
    val type: Type
    fun write(filename: String, content: List<String>)
    fun read(filename: String): List<String>

    enum class Type(val extension: String) {
        PLAIN_TEXT("txt"),
        DOCX("docx"),
    }
}
