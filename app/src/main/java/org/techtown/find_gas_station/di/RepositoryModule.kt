package org.techtown.find_gas_station.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.techtown.find_gas_station.data.local.RoomDB
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

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context): RoomDB {
        return RoomDB.getAppDatabase(context)
    }

    @Provides
    @Singleton
    fun provideSetRepository(
        roomDB: RoomDB // RoomDB 인스턴스를 Hilt로부터 주입받습니다.
    ): LocalRepository {
        return LocalRepositoryImpl(roomDB.setDao())
    }
}