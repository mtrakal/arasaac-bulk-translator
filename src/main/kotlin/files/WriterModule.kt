package files

import arasaac.model.Pictogram
import org.koin.core.qualifier.named
import org.koin.dsl.module

val writerModule = module {
    single<FileWriter<List<String>>>(named(FileWriter.Type.PLAIN_TEXT)) { FileWriterPlainTextImpl() }
    single<FileWriter<List<String>>>(named(FileWriter.Type.DOCX)) { FileWriterDocxImpl() }
    single<FileWriter<List<Pictogram>>>(named(FileWriter.Type.JSON)) { FileWriterJsonImpl(get()) }
}
