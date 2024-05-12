package org.techtown.find_gas_station.presentation.di

import org.techtown.find_gas_station.data.repository.GetOilAvgRepository
import org.techtown.find_gas_station.data.repository.StationInfoRepository

object RepositoryModule {
    fun provideStationInfoRepository() = StationInfoRepository()

    fun provideGetOilAvgRepository() = GetOilAvgRepository()

}