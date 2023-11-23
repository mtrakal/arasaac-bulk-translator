package arasaac.model

import PictogramId
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Pictogram(
    @SerialName("__v") val version: Int? = null,
    @SerialName("_id") val id: PictogramId,
    @SerialName("aac") val aac: Boolean,
    @SerialName("aacColor") val aacColor: Boolean,
    @SerialName("available") val available: Boolean,
    @SerialName("categories") val categories: List<String>,
    @SerialName("created") val created: String,
    @SerialName("desc") val desc: String? = null,
    @SerialName("downloads") val downloads: Int,
    @SerialName("hair") val hair: Boolean,
    @SerialName("keywords") var keywords: List<Keyword>,
    @SerialName("lastUpdated") var lastUpdated: String,
    @SerialName("published") val published: Boolean,
    @SerialName("schematic") val schematic: Boolean,
    @SerialName("sex") val sex: Boolean,
    @SerialName("skin") val skin: Boolean,
    @SerialName("synsets") val synsets: List<String>,
    @SerialName("tags") val tags: List<String>,
    @SerialName("validated") var validated: Boolean,
    @SerialName("violence") val violence: Boolean,
)
