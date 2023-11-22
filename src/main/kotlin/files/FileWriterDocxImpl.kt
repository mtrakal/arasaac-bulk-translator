package files

import okio.use
import org.apache.poi.xwpf.usermodel.ParagraphAlignment
import org.apache.poi.xwpf.usermodel.XWPFDocument
import org.apache.poi.xwpf.usermodel.XWPFParagraph
import java.io.File

class FileWriterDocxImpl : FileWriter {

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
        createParagraph().apply {
            alignment = ParagraphAlignment.LEFT
            // add text
            addRun(content)
        }
    }

    private fun XWPFParagraph.addRun(content: List<String>) {
        createRun().apply {
            fontSize = 8
            content.forEach { line ->
                setText(line)
                addBreak()
            }
        }
    }
}
