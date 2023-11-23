import arasaac.api.auth.ArasaacAuth
import arasaac.api.usecases.DownloadPictogramsUseCase
import arasaac.api.usecases.UploadPictogramsUseCase
import arasaac.model.Pictogram
import files.FileWriter
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.qualifier.named
import translator.PictogramKeywords
import translator.Translator
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@JvmInline
value class AllTranslatedPictograms(val pictograms: List<Pictogram>)

@JvmInline
value class ChangedOnlyPictograms(val pictograms: List<Pictogram>)

class App : KoinComponent {
    private val translator: Translator by inject()
    private val uploadPictogramsUseCase: UploadPictogramsUseCase by inject()
    private val downloadPictogramsUseCase: DownloadPictogramsUseCase by inject()
    private val jsonWriter: FileWriter<List<Pictogram>> by inject(named(FileWriter.Type.JSON))
    private val arasaacAuth: ArasaacAuth by inject(named(ArasaacAuth.Type.BEARER))

    init {
        val pictogramsEn = mutableListOf<Pictogram>()
        downloadPictogramsUseCase.loadPictograms("en") {
            pictogramsEn.addAll(it)
        }

        println("Enter target language (ISO 639-1):")
        val targetLanguage = readln()
        val targetPictograms = mutableListOf<Pictogram>()
        downloadPictogramsUseCase.loadPictograms(targetLanguage) {
            targetPictograms.addAll(it)
        }

        val keysForTranslate: PictogramKeywords = targetPictograms.mapNotNull { pictogram ->
            if (pictogram.validated || pictogram.keywords.isNotEmpty()) {
                return@mapNotNull null
            }

            val keywordsEn = pictogramsEn.firstOrNull { pictogram.id == it.id }?.keywords
            keywordsEn?.let { pictogram.id to it }
        }.toMap()

        val translatedKeys = translator.translate(keysForTranslate)

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val lastModified = LocalDateTime.now().format(formatter)

        val (
            translatedAllPictograms: AllTranslatedPictograms,
            changedOnlyPictograms: ChangedOnlyPictograms,
        ) = getTranslatedAllPictograms(
            targetPictograms,
            translatedKeys,
            lastModified,
        )

        jsonWriter.write("$targetLanguage.translated.json", translatedAllPictograms.pictograms)

        println("Do you really want upload all translated pictograms back to arasaac.org? (y/n)")
        val answer = readln().lowercase()
        if (answer == "y") {

            arasaacAuth.getAccessToken {
                if (it.token.isBlank()) {
                    error("Token is not provided!")
                }
            }

            uploadPictogramsUseCase.uploadPictograms(targetLanguage, changedOnlyPictograms)
        }
    }

    private fun getTranslatedAllPictograms(
        targetPictograms: MutableList<Pictogram>,
        translatedKeys: PictogramKeywords,
        lastModified: String,
    ): Pair<AllTranslatedPictograms, ChangedOnlyPictograms> {
        val changedOnlyPictograms = mutableListOf<Pictogram>()
        val pictogramList = AllTranslatedPictograms(
            targetPictograms.map { pictogram ->
                pictogram.copy().apply {
                    translatedKeys[id]
                        ?.takeIf { it.isNotEmpty() }
                        ?.let {
                            keywords = it
                            lastUpdated = lastModified
                        }?.also {
                            changedOnlyPictograms.add(this)
                        }
                }
            },
        )
        return Pair(pictogramList, ChangedOnlyPictograms(changedOnlyPictograms.toList()))
    }
}
