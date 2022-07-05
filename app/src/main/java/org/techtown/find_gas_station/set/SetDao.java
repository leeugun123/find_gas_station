package org.techtown.find_gas_station.set;

import static androidx.room.OnConflictStrategy.REPLACE;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SetDao {

    @Insert
    void insert(Set mainData);

    @Delete
    void delete(Set mainData);

    @Update
    void update(Set set);

    //@Query("SELECT * FROM Set")
    List<Set> getAll();

}
