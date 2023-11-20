package network

import arasaac.api.LoginApi
import arasaac.api.request.AccessTokenRequest
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route
import java.util.concurrent.atomic.AtomicBoolean

class TokenAuthenticator(
    private val accessTokenRequest: AccessTokenRequest,
    private val baseOkHttpClient: OkHttpClient,
    private val loginApi: LoginApi,
) : Authenticator {

    // AtomicBoolean in order to avoid race condition
    private var tokenRefreshInProgress: AtomicBoolean = AtomicBoolean(false)
    private var request: Request? = null

    override fun authenticate(route: Route?, response: Response): Request? {
        return runBlocking {
            request = null

            // Checking if a token refresh call is already in progress or not
            // The first request will enter the if block
            // Later requests will enter the else block
            if (!tokenRefreshInProgress.get()) {
                tokenRefreshInProgress.set(true)
                // Refreshing token
                refreshToken()
                request = buildRequest(response.request.newBuilder())
                tokenRefreshInProgress.set(false)
            } else {
                // Waiting for the ongoing request to finish
                // So that we don't refresh our token multiple times
                waitForRefresh(response)
            }

            // return null to stop retrying once responseCount returns 3 or above.
            if (responseCount(response) >= 3) {
                null
            } else {
                request
            }
        }
    }

    // Refresh your token here and save them.
    private suspend fun refreshToken() {
        runBlocking {
            loginApi.login(accessTokenRequest)
        }
    }

    // Queuing the requests with delay
    private suspend fun waitForRefresh(response: Response) {
        while (tokenRefreshInProgress.get()) {
            delay(100)
        }
        request = buildRequest(response.request.newBuilder())
    }

    private fun responseCount(response: Response?): Int {
        var result = 1
        while (response?.priorResponse != null && result <= 3) {
            result++
        }
        return result
    }

    // Build a new request with new access token
    private fun buildRequest(requestBuilder: Request.Builder): Request {
        return requestBuilder.apply {
            header(HEADER_CONTENT_TYPE, HEADER_CONTENT_TYPE_VALUE)
            ACCESS_TOKEN?.let {
                header(HEADER_AUTHORIZATION, HEADER_AUTHORIZATION_TYPE + ACCESS_TOKEN)
            }
        }
            .build()
    }

    companion object {
        const val HEADER_AUTHORIZATION = "Authorization"
        const val HEADER_CONTENT_TYPE = "Content-Type"
        const val HEADER_CONTENT_TYPE_VALUE = "application/json"
        const val HEADER_AUTHORIZATION_TYPE = "Bearer "

        var ACCESS_TOKEN: String? = null
    }
}
