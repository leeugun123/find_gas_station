package org.techtown.find_gas_station.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.techtown.find_gas_station.data.datasource.OilAvgRemoteDataSource
import org.techtown.find_gas_station.data.datasource.StationRemoteDataSource
import org.techtown.find_gas_station.data.remote.api.KakaoApi
import org.techtown.find_gas_station.data.remote.api.OpinetApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    @Provides
    @Singleton
    fun provideOilAvgRemoteDataSource(opinetApiService: OpinetApi): OilAvgRemoteDataSource {
        return OilAvgRemoteDataSource(opinetApiService)
    }

    @Provides
    @Singleton
    fun provideStationRemoteDataSource(
        opinetApiService: OpinetApi,
        kakaoApiService: KakaoApi
    ): StationRemoteDataSource {
        return StationRemoteDataSource(
            opinetApiService,
            kakaoApiService
        )
    }
}