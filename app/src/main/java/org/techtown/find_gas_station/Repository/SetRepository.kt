package org.techtown.find_gas_station.Repository

import androidx.lifecycle.LiveData
import org.techtown.find_gas_station.Data.set.OilData
import org.techtown.find_gas_station.Data.set.SetDao

class SetRepository(private val setDao : SetDao) {

    val allSets : LiveData<OilData> = setDao.getOilLocalData()

    suspend fun insert(oilData : OilData) {
        setDao.insert(oilData)
    }

    suspend fun deleteAll() {
        setDao.deleteAll()
    }

    suspend fun update(oilData : OilData) {
        setDao.update(oilData)
    }


}