package arasaac.api.auth

import arasaac.api.LoginApi
import arasaac.api.request.AccessTokenRequest
import arasaac.model.AccessToken
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.runBlocking

class ArasaacAuthUserPassImpl(
    private val accessToken: AccessToken,
    private val loginApi: LoginApi,
) : ArasaacAuth {
    override fun getAccessToken(result: (AccessToken) -> Unit) {
        login(result)
    }

    /**
     * Not working, because of security reasons - api calls not allowed outside of arasaac.org
     */
    private fun login(result: (AccessToken) -> Unit) {
        println("Enter your Arasaac username:")
        val username = readln()
        println("Enter your Arasaac password:")
        val password = readln()

        runBlocking {
            loginApi.login(AccessTokenRequest(username = username, password = password))
                .onSuccess {
                    println("Login success")
                    accessToken.token = this.data.accessToken
                    result(accessToken)
                }
                .onFailure {
                    error("Login failure")
                }
        }
    }
}
