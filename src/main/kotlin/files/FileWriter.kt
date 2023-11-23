package files

interface FileWriter<T> {
    val type: Type
    fun write(filename: String, content: T)
    fun read(filename: String): T

    enum class Type(val extension: String) {
        PLAIN_TEXT("txt"),
        DOCX("docx"),
        JSON("json"),
    }
}
