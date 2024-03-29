package org.techtown.find_gas_station.Util.Api

import com.google.gson.GsonBuilder
import org.techtown.find_gas_station.Retrofit.Kakao_RetrofitApi
import org.techtown.find_gas_station.Retrofit.Opinet_RetrofitApi
import org.techtown.find_gas_station.Util.Api.ApiBaseUrl.KAKAO_BASE_URL
import org.techtown.find_gas_station.Util.Api.ApiBaseUrl.OPINET_BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api_Instance {

    val gson = GsonBuilder().setLenient().create()

    private val opinetRetrofit = Retrofit.Builder()
        .baseUrl(OPINET_BASE_URL)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    private val kakaoRetrofit = Retrofit.Builder()
                        .baseUrl(KAKAO_BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()


    val opiRetrofitApi = opinetRetrofit.create(Opinet_RetrofitApi::class.java)!!
    val kakaoRetrofitApi = kakaoRetrofit.create(Kakao_RetrofitApi::class.java)!!


}