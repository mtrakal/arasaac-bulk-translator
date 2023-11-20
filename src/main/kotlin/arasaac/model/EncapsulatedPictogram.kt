package arasaac.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EncapsulatedPictogram(
    @SerialName("locale") val locale: String,
    @SerialName("pictogram") val pictogram: Pictogram,
)
