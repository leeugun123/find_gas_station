package org.techtown.find_gas_station.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.techtown.find_gas_station.data.local.RoomDB
import org.techtown.find_gas_station.data.local.SetDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocalDBModule {

    private const val DATABASE_NAME = "RoomDB-db"

    @Provides
    @Singleton
    fun provideRoomDB(@ApplicationContext context: Context): RoomDB {
        return Room.databaseBuilder(
            context.applicationContext,
            RoomDB::class.java,
            DATABASE_NAME
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    @Singleton
    fun provideSetDao(roomDB: RoomDB): SetDao {
        return roomDB.setDao()
    }
}