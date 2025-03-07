package org.techtown.find_gas_station.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.techtown.find_gas_station.data.datasource.OilAvgRemoteDataSource
import org.techtown.find_gas_station.data.mapper.toDomainList
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.domain.repositoy.OilAvgRepository

class OilAvgRepositoryImpl : OilAvgRepository {
    private val oilAvgRemoteDataSource = OilAvgRemoteDataSource()

    override suspend fun getOilAvg(prodCd: String): Flow<List<OilAveragePriceInfo>> = flow {
        emit(oilAvgRemoteDataSource.getOilAvg(prodCd).toDomainList())
    }
}