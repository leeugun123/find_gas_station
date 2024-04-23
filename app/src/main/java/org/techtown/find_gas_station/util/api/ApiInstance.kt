package org.techtown.find_gas_station.util.api

import com.google.gson.GsonBuilder
import org.techtown.find_gas_station.util.retrofit_interface.KakaoApi
import org.techtown.find_gas_station.util.retrofit_interface.OpinetApi
import org.techtown.find_gas_station.util.api.ApiBaseUrl.KAKAO_BASE_URL
import org.techtown.find_gas_station.util.api.ApiBaseUrl.OPINET_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance {

    val gson = GsonBuilder().setLenient().create()

    private val opinetRetrofit = Retrofit.Builder()
        .baseUrl(OPINET_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val kakaoRetrofit = Retrofit.Builder()
                        .baseUrl(KAKAO_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()


    val opiRetrofitApi = opinetRetrofit.create(OpinetApi::class.java)
    val kakaoRetrofitApi = kakaoRetrofit.create(KakaoApi::class.java)
}