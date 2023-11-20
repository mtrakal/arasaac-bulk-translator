package network

import arasaac.model.AccessToken
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class TokenInterceptor(private val accessToken: AccessToken) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val builder: Request.Builder = request.newBuilder().apply {
            header(TokenAuthenticator.HEADER_AUTHORIZATION, TokenAuthenticator.HEADER_AUTHORIZATION_TYPE + accessToken.token)
        }
        return chain.proceed(builder.build())
    }
}
