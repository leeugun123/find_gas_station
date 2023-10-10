package org.techtown.find_gas_station.set;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SetDao {

    @Query("SELECT * FROM set_table")
    Set getAll();

    @Query("SELECT * FROM set_table")
    LiveData<Set> getAllLiveData();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Set set);

    @Query("DELETE FROM set_table")
    void deleteAll();
    //모든 데이터를 지운다.

    @Update
    void update(Set set);


}
