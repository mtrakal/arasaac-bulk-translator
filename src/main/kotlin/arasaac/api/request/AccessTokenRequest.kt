package arasaac.api.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AccessTokenRequest(
    @SerialName("client_id") val clientId: String = "12345",
    @SerialName("client_secret") val clientSecret: String = "12345",
    @SerialName("grant_type") val grantType: String = "password",
    @SerialName("password") val password: String,
    @SerialName("scope") val scope: String = "offline_access",
    @SerialName("username") val username: String,
)
