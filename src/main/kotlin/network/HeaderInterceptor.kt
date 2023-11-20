package network

import okhttp3.Interceptor
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.Response

class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val url: String = request.url.toUrl().toString()

        val builder: Request.Builder = request.newBuilder().apply {
            if (request.body !is MultipartBody) {
                header("Content-Type", "application/json")
            }

//            if (url.contains(apiProvider.apiHost)) {
//                header("X-App-Token", apiProvider.appToken)
//                header("X-Api-Key", apiProvider.apiKey)
//            }
        }

        return chain.proceed(builder.build())
    }
}
