package org.techtown.find_gas_station.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.techtown.find_gas_station.data.remote.api.KakaoApi
import org.techtown.find_gas_station.data.remote.api.OpinetApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder().setLenient().create()
    }

    @Provides
    @Singleton
    fun provideOpinetApi(gson: Gson): OpinetApi {
        return Retrofit.Builder()
            .baseUrl("http://www.opinet.co.kr/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(OpinetApi::class.java)
    }

    @Provides
    @Singleton
    fun provideKakaoApi(gson: Gson): KakaoApi {
        return Retrofit.Builder()
            .baseUrl("https://apis-navi.kakaomobility.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(KakaoApi::class.java)
    }
}