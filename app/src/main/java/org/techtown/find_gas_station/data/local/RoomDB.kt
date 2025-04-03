package org.techtown.find_gas_station.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import org.techtown.find_gas_station.data.local.model.OilConditionEntity

@Database(entities = [OilConditionEntity::class], version = 4, exportSchema = false)
abstract class RoomDB : RoomDatabase() {

    abstract fun setDao(): SetDao

    companion object {

        private const val DATABASE_NAME = "RoomDB-db"

        @Volatile
        private var INSTANCE: RoomDB? = null

        fun getAppDatabase(context: Context): RoomDB {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        private fun buildDatabase(context: Context): RoomDB {
            return Room.databaseBuilder(
                context.applicationContext,
                RoomDB::class.java,
                DATABASE_NAME
            )
            .fallbackToDestructiveMigration() // 스키마 변경시 기존 데이터 삭제 -> 이전 사용자의 데이터를 모두 복원해야함.
            .build()
        }
    }
}