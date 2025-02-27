package org.techtown.find_gas_station.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.techtown.find_gas_station.data.local.model.OilDataEntity

@Dao
interface SetDao {

    @Query("SELECT * FROM OilDataEntity")
    suspend fun getOilLocalData(): OilDataEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oilData: OilDataEntity)

    @Query("DELETE FROM OilDataEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(oilData: OilDataEntity)
}