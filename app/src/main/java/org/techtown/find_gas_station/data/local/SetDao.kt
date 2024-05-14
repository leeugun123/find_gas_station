package org.techtown.find_gas_station.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import org.techtown.find_gas_station.data.local.model.OilData

@Dao
interface SetDao {

    @Query("SELECT * FROM OilData")
    suspend fun getOilLocalData(): OilData

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(oilData: OilData)

    @Query("DELETE FROM OilData")
    suspend fun deleteAll()

    @Update
    suspend fun update(oilData: OilData)
}