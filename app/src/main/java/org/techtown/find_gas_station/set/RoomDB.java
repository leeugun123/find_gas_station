package org.techtown.find_gas_station.set;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Set.class}, version = 1,exportSchema = true)
public abstract class RoomDB extends RoomDatabase {

    private static RoomDB INSTANCE;

    public abstract SetDao setDao();

    //디비객체생성 가져오기
    public static RoomDB getAppDatabase(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context, RoomDB.class , "RoomDB-db")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();


        }
        return  INSTANCE;
    }



}
