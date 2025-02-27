package org.techtown.find_gas_station.data.repository

import org.techtown.find_gas_station.data.local.SetDao
import org.techtown.find_gas_station.data.local.model.OilDataEntity

class LocalRepository(private val setDao: SetDao) {

    suspend fun getOilLocalData() = setDao.getOilLocalData()

    suspend fun insert(oilDataEntity: OilDataEntity) {
        setDao.insert(oilDataEntity)
    }

    suspend fun deleteAll() {
        setDao.deleteAll()
    }

}