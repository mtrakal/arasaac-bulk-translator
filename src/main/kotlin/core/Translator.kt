package core

import PictogramId
import arasaac.model.Keyword
import java.io.File

class Translator {

    companion object {
        const val FILE_NAME = "translate_this_file"
        const val FILE_EXTENSION = ".txt"
    }

    fun prepareFileForTranslate(
        pictoKeywords: Map<PictogramId, List<Keyword>>,
        filename: String = "$FILE_NAME$FILE_EXTENSION",
    ) {
        val stringsToTranslate = convertToStrings(pictoKeywords)

        File(filename).writeText(stringsToTranslate.joinToString("\n"))
    }

    fun loadTranslatedFile(
        pictoKeywords: Map<PictogramId, List<Keyword>>,
        filename: String = "$FILE_NAME$FILE_EXTENSION",
    ): Map<PictogramId, List<Keyword>> {
        val translatedStrings = File(filename).readText().split("\n")
        return convertFromStrings(pictoKeywords, translatedStrings)
    }

    private fun convertFromStrings(
        pictograms: Map<PictogramId, List<Keyword>>,
        translatedStrings: List<String>,
    ): Map<PictogramId, List<Keyword>> = pictograms.map { (pictoId, keywords) ->

        val translatedKeywords = translatedStrings.filter { it.startsWith("$pictoId:") }.groupBy { it.split("::").first() }.let {
            it.map {
                val keywordText = getText(it.value, Type.KEYWORD).orEmpty()
                val pluralText = getText(it.value, Type.PLURAL)
                val meaningText = null // getText(it.value, Type.MEANING)
                Triple(keywordText, pluralText, meaningText)
            }
        }

        pictoId to keywords.mapIndexed { index, keyword ->
            val (keywordText, pluralText, meaningText) = translatedKeywords[index]
            keyword.copy(
                keyword = keywordText,
                plural = pluralText,
                meaning = meaningText,
            )
        }
    }.toMap()

    private fun getText(translatedStrings: List<String>, type: Type): String? =
        translatedStrings.firstOrNull { it.contains("${type.id}:::") }?.split(":::")?.last()?.trim()?.takeIf { it.isNotBlank() }

    private fun convertToStrings(pictoKeywords: Map<PictogramId, List<Keyword>>): List<String> =
        pictoKeywords.flatMap { (pictoId, keywords) ->
            keywords.flatMapIndexed { index, keyword ->
                listOf(
                    "$pictoId:$index::${Type.KEYWORD.id}::: ${keyword.keyword}",
                    "$pictoId:$index::${Type.PLURAL.id}::: ${keyword.plural.orEmpty()}",
//                    "$pictoId:$index::${Type.MEANING.id}::: ${keyword.meaning.orEmpty()}",
                )
            }
        }
}
