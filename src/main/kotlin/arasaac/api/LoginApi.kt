package arasaac.api

import arasaac.api.request.AccessTokenRequest
import arasaac.api.response.AccessTokenResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginApi {

    @POST("/oauth/token")
    suspend fun login(@Body body: AccessTokenRequest): ApiResponse<AccessTokenResponse>
}
