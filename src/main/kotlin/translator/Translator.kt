package translator

import PictogramId
import arasaac.model.Keyword

typealias PictogramKeywords = Map<PictogramId, List<Keyword>>
interface Translator {
    fun translate(pictoKeywords: PictogramKeywords): PictogramKeywords
}
