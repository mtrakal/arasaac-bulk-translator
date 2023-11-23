package arasaac.api.usecases

import ChangedOnlyPictograms
import arasaac.api.PictogramsApi
import arasaac.model.EncapsulatedPictogram
import com.skydoves.sandwich.ApiResponse
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import com.skydoves.sandwich.retrofit.errorBody
import com.skydoves.sandwich.retrofit.statusCode
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import logging.Logger
import kotlin.time.Duration.Companion.milliseconds

class UploadPictogramsUseCaseImpl(
    private val pictogramsApi: PictogramsApi,
    private val logger: Logger,
) :
    UploadPictogramsUseCase {
    override fun uploadPictograms(targetLanguage: String, pictograms: ChangedOnlyPictograms) {
        runBlocking {
            pictograms.pictograms.forEachIndexed { index, pictogram ->
                pictogramsApi.putPictogram(EncapsulatedPictogram(targetLanguage, pictogram))
                    .onSuccess {
                        println("Pictogram ${pictogram._id} updated")
                    }
                    .onFailure {
                        this.handleError()
                    }
                // Don't overload server
                delay(50.milliseconds)
                print("Progress ${index + 1}/${pictograms.pictograms.size}\r")
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
}
