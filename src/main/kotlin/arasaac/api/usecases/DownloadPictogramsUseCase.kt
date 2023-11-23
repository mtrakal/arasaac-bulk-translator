package arasaac.api.usecases

import arasaac.model.Pictogram

interface DownloadPictogramsUseCase {
    fun loadPictograms(locale: String, onSuccess: (List<Pictogram>) -> Unit)
}
