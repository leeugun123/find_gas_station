package org.techtown.find_gas_station.di

import org.techtown.find_gas_station.data.repository.OilAvgRepositoryImpl
import org.techtown.find_gas_station.data.repository.StationInfoRepositoryImpl

object RepositoryModule {
    fun provideStationInfoRepository() = StationInfoRepositoryImpl()
    fun provideGetOilAvgRepository() = OilAvgRepositoryImpl()
}
