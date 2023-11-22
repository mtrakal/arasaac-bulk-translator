import arasaac.api.apiModule
import core.appModule
import core.loggingModule
import network.networkModule
import org.koin.core.context.startKoin
import translator.translatorModule
import kotlin.collections.set

typealias PictogramId = Int

fun main(args: Array<String>) {
    startKoin {
        modules(
            apiModule,
            appModule,
            loggingModule,
            networkModule,
            translatorModule,
        )
    }
    App()
}
