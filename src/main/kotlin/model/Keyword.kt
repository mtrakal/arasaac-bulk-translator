package model

import kotlinx.serialization.Serializable

@Serializable
data class Keyword(
    val hasLocution: Boolean,
    var keyword: String,
    var meaning: String? = null,
    var plural: String? = null,
    val type: Int? = null
)