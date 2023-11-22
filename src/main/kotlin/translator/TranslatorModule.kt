package translator

import files.FileWriter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val translatorModule = module {
    single<Translator> { TranslatorManualImpl(get(named(FileWriter.Type.DOCX))) }
}
