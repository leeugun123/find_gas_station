package org.techtown.find_gas_station.Retrofit

import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.Data.kakao.Request.DirectionRequest
import org.techtown.find_gas_station.Data.kakao.Response.DirectionResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface Kakao_RetrofitApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: KakaoAK " + BuildConfig.KAKAO_API_KEY
    )
    @POST("v1/destinations/directions")
    fun getMultiDirections(@Body request: DirectionRequest?): Call<DirectionResponse?>? //다중 목적지
}