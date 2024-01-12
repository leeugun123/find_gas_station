package org.techtown.find_gas_station.Data.set

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface SetDao {

    @Query("SELECT * FROM OilData")
    fun getOilLocalData() : LiveData<OilData>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oilData : OilData)

    @Query("DELETE FROM OilData")
    suspend fun deleteAll()

    @Update
    suspend fun update(oilData : OilData)


}