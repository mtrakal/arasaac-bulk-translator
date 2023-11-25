package arasaac.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Keyword(
    @SerialName("hasLocution") val hasLocution: Boolean = false,
    @SerialName("keyword") var keyword: String,
    @SerialName("meaning") var meaning: String? = null,
    @SerialName("plural") var plural: String? = null,
    @SerialName("type") val type: Int? = null,
)
