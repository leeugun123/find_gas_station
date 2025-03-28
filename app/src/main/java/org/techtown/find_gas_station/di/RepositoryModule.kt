package org.techtown.find_gas_station.di

import android.content.Context
import org.techtown.find_gas_station.data.local.RoomDB
import org.techtown.find_gas_station.data.repository.LocalRepositoryImpl
import org.techtown.find_gas_station.data.repository.OilAvgRepositoryImpl
import org.techtown.find_gas_station.data.repository.StationInfoRepositoryImpl
import org.techtown.find_gas_station.domain.repositoy.OilAvgRepository

object RepositoryModule {

    private lateinit var appContext: Context

    fun init(context : Context){
        appContext = context.applicationContext
    }

    fun provideStationInfoRepository() = StationInfoRepositoryImpl()
    fun provideGetOilAvgRepository() = OilAvgRepositoryImpl()
    fun provideSetRepository() = LocalRepositoryImpl(RoomDB.getAppDatabase(appContext).setDao())
}
