package org.techtown.find_gas_station.domain.repositoy

import org.techtown.find_gas_station.domain.model.OilCondition

interface LocalRepository {
    suspend fun getOilLocalData() : OilCondition?
    suspend fun insert(oilCondition: OilCondition)
    suspend fun deleteAll()
}