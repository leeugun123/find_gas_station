package org.techtown.find_gas_station.set;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

public interface SetDao {

    @Query("SELECT * FROM set_table")
    LiveData<List<Set>> getAll();

    @Insert
    void insert(Set set);

    @Query("DELETE FROM set_table")
    void deleteAll();

    @Update
    void update(Set set);
}
