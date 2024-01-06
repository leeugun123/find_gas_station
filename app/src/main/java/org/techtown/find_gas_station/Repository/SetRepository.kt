package org.techtown.find_gas_station.Repository

import androidx.lifecycle.LiveData
import org.techtown.find_gas_station.set.SetDao

class SetRepository(private val setDao : SetDao) {

    val allSets : LiveData<org.techtown.find_gas_station.set.Set> = setDao.getOilLocalData()

    suspend fun insert(set : org.techtown.find_gas_station.set.Set) {
        setDao.insert(set)
    }

    suspend fun deleteAll() {
        setDao.deleteAll()
    }

    suspend fun update(set : org.techtown.find_gas_station.set.Set) {
        setDao.update(set)
    }


}