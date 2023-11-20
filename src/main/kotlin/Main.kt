import arasaac.api.apiModule
import core.loggingModule
import network.networkModule
import org.koin.core.context.startKoin
import kotlin.collections.set

typealias PictogramId = Int

fun main(args: Array<String>) {
    startKoin {
        modules(
            apiModule,
            loggingModule,
            networkModule,
        )
    }
    App()
}
