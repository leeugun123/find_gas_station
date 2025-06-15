package org.techtown.find_gas_station.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import org.techtown.find_gas_station.data.datasource.OilAvgRemoteDataSource
import org.techtown.find_gas_station.data.datasource.StationRemoteDataSource
import org.techtown.find_gas_station.data.local.SetDao
import org.techtown.find_gas_station.data.repository.LocalRepositoryImpl
import org.techtown.find_gas_station.data.repository.OilAvgRepositoryImpl
import org.techtown.find_gas_station.data.repository.StationInfoRepositoryImpl
import org.techtown.find_gas_station.domain.repositoy.LocalRepository
import org.techtown.find_gas_station.domain.repositoy.OilAvgRepository
import org.techtown.find_gas_station.domain.repositoy.StationInfoRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindStationInfoRepository(
        stationInfoRepositoryImpl : StationInfoRepositoryImpl
    ): StationInfoRepository

    @Binds
    abstract fun bindOilAvgRepository(
        oilAvgRepositoryImpl : OilAvgRepositoryImpl
    ): OilAvgRepository

    @Binds
    abstract fun bindLocalRepository(
        localRepositoryImpl :  LocalRepositoryImpl
    ): LocalRepository
}