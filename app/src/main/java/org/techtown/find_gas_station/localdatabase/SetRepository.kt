package org.techtown.find_gas_station.localdatabase

import org.techtown.find_gas_station.localdatabase.OilData
import org.techtown.find_gas_station.localdatabase.SetDao

class SetRepository(private val setDao : SetDao) {

    suspend fun getOilLocalData() = setDao.getOilLocalData()


    suspend fun insert(oilData : OilData) {
        setDao.insert(oilData)
    }

    suspend fun deleteAll() {
        setDao.deleteAll()
    }

}