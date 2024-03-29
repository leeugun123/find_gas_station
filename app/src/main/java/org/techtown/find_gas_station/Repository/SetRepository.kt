package org.techtown.find_gas_station.Repository

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.set.OilData
import org.techtown.find_gas_station.Data.set.SetDao

class SetRepository(private val setDao : SetDao) {

    suspend fun getOilLocalData() = setDao.getOilLocalData()


    suspend fun insert(oilData : OilData) {
        setDao.insert(oilData)
    }

    suspend fun deleteAll() {
        setDao.deleteAll()
    }

}