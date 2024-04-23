package org.techtown.find_gas_station.data.repository


object RepositoryModule {
    fun provideStationInfoRepository(): StationInfoRepository = StationInfoRepository()

    fun provideGetOilAvgRepository(): GetOilAvgRepository = GetOilAvgRepository()

}