import arasaac.api.LoginApi
import arasaac.api.PictogramsApi
import arasaac.api.request.AccessTokenRequest
import arasaac.model.AccessToken
import arasaac.model.EncapsulatedPictogram
import arasaac.model.Keyword
import arasaac.model.Pictogram
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.retrofit.statusCode
import core.Logger
import core.Translator
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

class App : KoinComponent {
    private val loginApi: LoginApi by inject()
    private val pictogramsApi: PictogramsApi by inject()
    private val accessToken: AccessToken by inject()
    private val logger: Logger by inject()

    private val jsonOutputSerializer = Json {
        prettyPrint = true
    }

    init {
//        login()
        loginToken()

        val en = mutableListOf<Pictogram>()
        loadPictograms("en") {
            en.addAll(it)
        }

        println("Enter target language (ISO 639-1):")
        val targetLanguage = readln()
        val target = mutableListOf<Pictogram>()
        loadPictograms(targetLanguage) {
            target.addAll(it)
        }

        val keysForTranslate = mutableMapOf<PictogramId, List<Keyword>>()

        val pictograms = target.onEach { pictogram ->
            pictogram.apply {
                if (keywords.isEmpty()) {
                    val keywordsOrigin = en.firstOrNull { pictogram._id == it._id }?.keywords.orEmpty()
                    validated = false
                    keysForTranslate[_id] = keywordsOrigin
                }
            }
        }

        Translator().prepareFileForTranslate(keysForTranslate)

        println("Translate file: ${Translator.FILE_NAME}${Translator.FILE_EXTENSION} and after press any key to continue...")
        System.`in`.read()
        val translatedKeys = Translator().loadTranslatedFile(keysForTranslate)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val now = LocalDateTime.now().format(formatter)

        pictograms.forEach { pictogram ->
            pictogram.apply {
                translatedKeys[_id]?.let {
                    keywords = it
                    lastUpdated = now
                }
            }
        }

        storeJsonToFile("$targetLanguage.translated.json", jsonOutputSerializer.encodeToString(pictograms))

        println("Do you really upload all translated pictograms back to arasaac.org? (y/n)")
        val answer = readln()
        if (answer == "y") {
            uploadPictograms(targetLanguage, pictograms)
        }
    }

    private fun uploadPictograms(targetLanguage: String, pictograms: MutableList<Pictogram>) {
        runBlocking {
            pictograms.forEachIndexed { index, pictogram ->
                pictogramsApi.putPictogram(EncapsulatedPictogram(targetLanguage, pictogram))
                    .onSuccess {
                        println("Pictogram ${pictogram._id} updated")
                    }
                    .onFailure {
                        this.handleError()
                    }
                // Don't overload server
                delay(50.milliseconds)
                print("Progress ${pictograms.indexOf(pictogram) + 1}/${pictograms.size}\r")
            }
        }
    }

    private fun ApiResponse.Failure<*>.handleError() {
        when (this) {
            is ApiResponse.Failure.Error -> {
                logger.e("Error: ${this.statusCode}: ${this.errorBody?.toString()}")
            }

            is ApiResponse.Failure.Exception -> {
                logger.e(this.throwable)
            }
        }
    }

    private fun loadPictograms(locale: String, onSuccess: (List<Pictogram>) -> Unit) {
        runBlocking {
            pictogramsApi.getPictograms(locale)
                .onSuccess {
                    println("Pictograms in '$locale' loaded")
                    storeJsonToFile("$locale.json", this.data.toJson)
                    onSuccess(this.data)
                }
                .onFailure {
                    error("Pictograms in '$locale' not loaded")
                }
        }
    }

    /**
     * Not working, because of security reasons - api calls not allowed outside of arasaac.org
     */
    private fun login() {
        println("Enter your Arasaac username:")
        val username = readln()
        println("Enter your Arasaac password:")
        val password = readln()

        runBlocking {
            loginApi.login(AccessTokenRequest(username = username, password = password))
                .onSuccess {
                    println("Login success")
                }
                .onFailure {
                    error("Login failure")
                }
        }
    }

    private fun loginToken() {
        println("Enter your Bearer token (from Developer console):")
        accessToken.token = readln()
    }

    private fun readFileAndParseToJson(filename: String): List<Pictogram> {
        readJsonFromFile(filename).let { json ->
            return Json.decodeFromString(json)
        }
    }

    private fun readJsonFromFile(filename: String): String {
        val file = File(filename)
        return file.readText(Charsets.UTF_8)
    }

    private fun storeJsonToFile(filename: String, json: String) {
        val file = File(filename)
        file.writeText(json)
    }

    private val List<Pictogram>.toJson
        get() = jsonOutputSerializer.encodeToString(this)
}
