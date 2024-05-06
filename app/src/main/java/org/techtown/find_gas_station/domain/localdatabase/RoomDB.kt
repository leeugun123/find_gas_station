package org.techtown.find_gas_station.domain.localdatabase

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [OilData::class], version = 2, exportSchema = false)
abstract class RoomDB : RoomDatabase() {

    abstract fun setDao(): SetDao

    companion object {
        @Volatile
        private var INSTANCE: RoomDB? = null
        fun getAppDatabase(context: Context): RoomDB {

            return INSTANCE ?: synchronized(lock = this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "RoomDB-db"
                ).addMigrations().fallbackToDestructiveMigration().build()

                INSTANCE = instance
                instance
            }
        }
    }

}