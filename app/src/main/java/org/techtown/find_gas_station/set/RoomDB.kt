package org.techtown.find_gas_station.set

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import kotlin.collections.Set

@Database(entities = [Set::class], version = 1, exportSchema = true)
abstract class RoomDB : RoomDatabase() {

    abstract fun setDao(): SetDao

    //싱글톤 패턴 , 앱의 생명주기 동안 한 번만 생성되어야 함.

    companion object {

        @Volatile
        private var INSTANCE : RoomDB? = null
        fun getAppDatabase(context: Context): RoomDB {

            //synchronized 키워드를 통해 여러 쓰레드가 동시에 이 블록에 들어갈 수 없게 함.
            return INSTANCE ?: synchronized(lock = this){

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RoomDB::class.java,
                    "RoomDB-db"
                ).build()
                INSTANCE = instance
                instance
            }

        }

    }

}