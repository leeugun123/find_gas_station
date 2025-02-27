package org.techtown.find_gas_station.di

import org.techtown.find_gas_station.data.repository.OilAvgRepository
import org.techtown.find_gas_station.data.repository.StationInfoRepository

object RepositoryModule {
    fun provideStationInfoRepository() = StationInfoRepository()
    fun provideGetOilAvgRepository() = OilAvgRepository()
}