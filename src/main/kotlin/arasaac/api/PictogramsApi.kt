package arasaac.api

import arasaac.model.EncapsulatedPictogram
import arasaac.model.Pictogram
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface PictogramsApi {

    @GET("/api/pictograms/{locale}")
    suspend fun getPictograms(@Path("locale") locale: String): ApiResponse<List<Pictogram>>

    @PUT("/api/pictograms")
    suspend fun putPictogram(@Body pictogram: EncapsulatedPictogram): ApiResponse<Pictogram>
}
