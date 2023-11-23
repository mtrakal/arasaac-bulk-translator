package files

import okio.use
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import java.io.File

class FileWriterDocxImpl : FileWriter<List<String>> {

    override val type = FileWriter.Type.DOCX
    override fun write(filename: String, content: List<String>) {
        XWPFDocument().apply {
            addParagraph(content)
        }.save(filename)
    }

    override fun read(filename: String): List<String> {
        XWPFDocument(File(filename).inputStream()).use { doc ->
            return doc.paragraphs.map { it.text }
        }
    }

    private fun XWPFDocument.save(filename: String) = File(filename).outputStream().use {
        write(it)
    }

    private fun XWPFDocument.addParagraph(content: List<String>) {
        // creating a paragraph in our document and setting its alignment
        content.forEach { line ->
            createParagraph().apply {
                alignment = ParagraphAlignment.LEFT
                // add text
                addRun(line)
            }
        }
    }

    private fun XWPFParagraph.addRun(content: String) {
        createRun().apply {
            fontSize = 8
            setText(content)
        }
    }
}
