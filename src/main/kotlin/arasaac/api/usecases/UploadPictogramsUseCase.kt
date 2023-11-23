package arasaac.api.usecases

import ChangedOnlyPictograms

interface UploadPictogramsUseCase {
    fun uploadPictograms(targetLanguage: String, pictograms: ChangedOnlyPictograms)
}
