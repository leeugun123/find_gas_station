package org.techtown.find_gas_station.Repository

import androidx.lifecycle.LiveData
import org.techtown.find_gas_station.Data.set.Set
import org.techtown.find_gas_station.Data.set.SetDao

class SetRepository(private val setDao : SetDao) {

    val allSets : LiveData<Set> = setDao.getOilLocalData()

    suspend fun insert(set : Set) {
        setDao.insert(set)
    }

    suspend fun deleteAll() {
        setDao.deleteAll()
    }

    suspend fun update(set : Set) {
        setDao.update(set)
    }


}