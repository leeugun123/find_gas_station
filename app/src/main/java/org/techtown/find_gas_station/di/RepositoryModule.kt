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
object RepositoryModule {

    @Binds
    @Singleton
    fun bindStationInfoRepository(
        stationRemoteDataSource: StationRemoteDataSource
    ): StationInfoRepository {
        return StationInfoRepositoryImpl(stationRemoteDataSource)
    }

    @Binds
    @Singleton
    fun bindOilAvgRepository(
        oilAvgRemoteDataSource: OilAvgRemoteDataSource
    ): OilAvgRepository {
        return OilAvgRepositoryImpl(oilAvgRemoteDataSource)
    }

    @Binds
    @Singleton
    fun bindLocalRepository(
        setDao: SetDao
    ): LocalRepository {
        return LocalRepositoryImpl(setDao)
    }
}