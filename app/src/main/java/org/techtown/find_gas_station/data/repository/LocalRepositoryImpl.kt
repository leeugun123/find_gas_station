package org.techtown.find_gas_station.data.repository

import org.techtown.find_gas_station.data.local.SetDao
import org.techtown.find_gas_station.data.toDomain
import org.techtown.find_gas_station.data.toEntity
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.repositoy.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl(private val setDao: SetDao) : LocalRepository {

    override suspend fun getOilLocalData() = setDao.getOilLocalData()?.toDomain()

    override suspend fun insert(oilCondition: OilCondition) {
        setDao.insert(oilCondition.toEntity())
    }

    override suspend fun deleteAll() {
        setDao.deleteAll()
    }
}