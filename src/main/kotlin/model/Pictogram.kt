package model

import PictogramId
import kotlinx.serialization.Serializable

@Serializable
data class Pictogram(
    val __v: Int? = null,
    val _id: PictogramId,
    val aac: Boolean,
    val aacColor: Boolean,
    val available: Boolean,
    val categories: List<String>,
    val created: String,
    val desc: String? = null,
    val downloads: Int,
    val hair: Boolean,
    var keywords: List<Keyword>,
    val lastUpdated: String,
    val published: Boolean,
    val schematic: Boolean,
    val sex: Boolean,
    val skin: Boolean,
    val synsets: List<String>,
    val tags: List<String>,
    var validated: Boolean,
    val violence: Boolean
)