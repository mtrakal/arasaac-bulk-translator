package arasaac.api.auth

import arasaac.model.AccessToken

interface ArasaacAuth {
    fun getAccessToken(result: (AccessToken) -> Unit)

    enum class Type {
        USER_PASS,
        BEARER,
    }
}
