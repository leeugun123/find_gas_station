package org.techtown.find_gas_station.set

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface SetDao {

    @Query("SELECT * FROM set_table")
    fun getOilLocalData() : LiveData<Set>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(set : Set)

    @Query("DELETE FROM set_table")
    suspend fun deleteAll()

    @Update
    suspend fun update(set : Set)


}