package org.techtown.find_gas_station.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.techtown.find_gas_station.data.local.entity.OilConditionEntity

@Dao
interface SetDao {

    @Query("SELECT * FROM OilConditionEntity")
    suspend fun getOilLocalData(): OilConditionEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oilData: OilConditionEntity)

    @Query("DELETE FROM OilConditionEntity")
    suspend fun deleteAll()

    @Update
    suspend fun update(oilData: OilConditionEntity)
}