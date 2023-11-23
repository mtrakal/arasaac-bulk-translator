package arasaac.api.auth

import arasaac.model.AccessToken

class ArasaacAuthBearerImpl(private val accessToken: AccessToken) : ArasaacAuth {
    override fun getAccessToken(result: (AccessToken) -> Unit) {
        println("Enter your Bearer token (from Developer console):")
        accessToken.token = readln()
        result(accessToken)
    }
}
