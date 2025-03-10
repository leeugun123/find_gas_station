package org.techtown.find_gas_station.data.remote.api

import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.data.remote.model.station.kakao.DirectionRequest
import org.techtown.find_gas_station.data.remote.model.station.kakao.DirectionResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface KakaoApi {
    @Headers(
        "Content-Type: application/json",
        "Authorization: KakaoAK " + BuildConfig.KAKAO_API_KEY
    )
    @POST("v1/destinations/directions")
    suspend fun getMultiDirections(@Body request : DirectionRequest) : Response<DirectionResponse>
    //다중 목적지
}