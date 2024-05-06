package org.techtown.find_gas_station.presentation.di

import org.techtown.find_gas_station.data.repository.GetOilAvgRepository
import org.techtown.find_gas_station.data.repository.StationInfoRepositoryImpl


object RepositoryModule {
    fun provideStationInfoRepository() = StationInfoRepositoryImpl()

    fun provideGetOilAvgRepository() = GetOilAvgRepository()

}