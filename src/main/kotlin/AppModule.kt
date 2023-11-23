
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val appModule = module {
    single<Json> {
        Json {
            prettyPrint = true
        }
    }
}
