import arasaac.api.apiModule
import arasaac.api.auth.authModule
import files.writerModule
import logging.loggingModule
import network.networkModule
import org.koin.core.context.startKoin
import translator.translatorModule

typealias PictogramId = Int

fun main(args: Array<String>) {
    startKoin {
        modules(
            apiModule,
            appModule,
            authModule,
            loggingModule,
            networkModule,
            translatorModule,
            writerModule,
        )
    }
    App()
}
