import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.koin.dsl.module

@OptIn(ExperimentalSerializationApi::class)
val appModule = module {
    single<Json> {
        Json {
            encodeDefaults = true
            explicitNulls = false
            prettyPrint = true
        }
    }
}
