package org.techtown.find_gas_station.set;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = (Set.class), version = 1,exportSchema = true)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB database;

    private static String DATABASE_NAME = "oil_database";

    public synchronized static RoomDB getInstance(Context context){

        if (database == null)
        {
            database = Room.databaseBuilder(context.getApplicationContext(), RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;

    }

    public abstract SetDao setDao();

}
