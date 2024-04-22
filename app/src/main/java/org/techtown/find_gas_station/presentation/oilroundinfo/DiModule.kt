package org.techtown.find_gas_station.presentation.oilroundinfo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {
    @Singleton
    @Provides
    fun provideStationInfoRepository(): StationInfoRepository = StationInfoRepository()
}