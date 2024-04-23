package org.techtown.find_gas_station.presentation.ui.oilroundinfo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.techtown.find_gas_station.data.repository.StationInfoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DiModule {
    @Singleton
    @Provides
    fun provideStationInfoRepository(): StationInfoRepository = StationInfoRepository()
}