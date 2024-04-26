package org.techtown.find_gas_station.data.repository.module

import com.google.gson.GsonBuilder
import org.techtown.find_gas_station.util.api_interface.KakaoApi
import org.techtown.find_gas_station.util.api_interface.OpinetApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiModule {

    private val gson = GsonBuilder().setLenient().create()

    fun provideOpinetApi(): OpinetApi = Retrofit.Builder()
        .baseUrl("http:///www.opinet.co.kr/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build().create(OpinetApi::class.java)

    fun provideKakaoApi(): KakaoApi = Retrofit.Builder()
        .baseUrl("https://apis-navi.kakaomobility.com/")
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build().create(KakaoApi::class.java)
}