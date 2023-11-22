package core

import files.FileWriter
import files.FileWriterDocxImpl
import files.FileWriterPlainTextImpl
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {
    single<Json> {
        Json {
            prettyPrint = true
        }
    }
    single<FileWriter>(named(FileWriter.Type.PLAIN_TEXT)) { FileWriterPlainTextImpl() }
    single<FileWriter>(named(FileWriter.Type.DOCX)) { FileWriterDocxImpl() }
}
