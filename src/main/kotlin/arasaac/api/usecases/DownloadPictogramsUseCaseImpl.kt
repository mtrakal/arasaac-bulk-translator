package arasaac.api.usecases

import arasaac.api.PictogramsApi
import arasaac.model.Pictogram
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import files.FileWriter
import kotlinx.coroutines.runBlocking

class DownloadPictogramsUseCaseImpl(
    private val pictogramsApi: PictogramsApi,
    private val jsonWriter: FileWriter<List<Pictogram>>,

) : DownloadPictogramsUseCase {
    override fun loadPictograms(locale: String, onSuccess: (List<Pictogram>) -> Unit) {
        runBlocking {
            pictogramsApi.getPictograms(locale)
                .onSuccess {
                    println("Pictograms in '$locale' loaded")
                    jsonWriter.write("$locale.json", this.data)
                    onSuccess(this.data)
                }
                .onFailure {
                    error("Pictograms in '$locale' not loaded")
                }
        }
    }
}
