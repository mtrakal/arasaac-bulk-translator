package translator

import files.FileWriter
import model.Type

class TranslatorManualImpl(private val fileWriter: FileWriter<List<String>>) : Translator {

    companion object {
        const val FILE_NAME = "translate_this_file"
    }

    private val fileExtension: String
        get() = ".${fileWriter.type.extension}"

    override fun translate(pictoKeywords: PictogramKeywords): PictogramKeywords {
        prepareFileForTranslate(pictoKeywords)

        println("Translate file: ${FILE_NAME}$fileExtension and after press any key to continue...")
        System.`in`.read()

        return loadTranslatedFile(pictoKeywords)
    }

    private fun prepareFileForTranslate(
        pictoKeywords: PictogramKeywords,
        filename: String = "$FILE_NAME$fileExtension",
    ) {
        val stringsToTranslate = convertToStrings(pictoKeywords)

        fileWriter.write(filename, stringsToTranslate)
    }

    private fun loadTranslatedFile(
        pictoKeywords: PictogramKeywords,
        filename: String = "$FILE_NAME$fileExtension",
    ): PictogramKeywords {
        val translatedStrings = fileWriter.read(filename)
        return convertFromStrings(pictoKeywords, translatedStrings)
    }

    private fun convertFromStrings(
        pictograms: PictogramKeywords,
        translatedStrings: List<String>,
    ): PictogramKeywords = pictograms.map { (pictoId, keywords) ->

        val translatedKeywords = translatedStrings.filter { it.startsWith("$pictoId:") }.groupBy { it.split("::").first() }.let {
            it.map {
                val keywordText = getText(it.value, Type.KEYWORD).orEmpty()
                val pluralText = getText(it.value, Type.PLURAL)
                Pair(keywordText, pluralText)
            }
        }

        pictoId to keywords.mapIndexed { index, keyword ->
            val (keywordText, pluralText) = translatedKeywords[index]
            keyword.copy(
                hasLocution = false,
                keyword = keywordText,
                plural = pluralText,
                meaning = null,
            )
        }
    }.toMap()

    private fun getText(translatedStrings: List<String>, type: Type): String? =
        translatedStrings.firstOrNull { it.contains("${type.id}:::") }?.split(":::")?.last()?.trim()?.takeIf { it.isNotBlank() }

    private fun convertToStrings(pictoKeywords: PictogramKeywords): List<String> =
        pictoKeywords.flatMap { (pictoId, keywords) ->
            keywords.flatMapIndexed { index, keyword ->
                listOf(
                    "$pictoId:$index::${Type.KEYWORD.id}::: ${keyword.keyword}",
                    "$pictoId:$index::${Type.PLURAL.id}::: ${keyword.plural.orEmpty()}",
                )
            }
        }
}
