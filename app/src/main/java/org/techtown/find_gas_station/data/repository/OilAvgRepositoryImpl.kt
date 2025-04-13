package org.techtown.find_gas_station.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.techtown.find_gas_station.data.datasource.OilAvgRemoteDataSource
import org.techtown.find_gas_station.data.toDomainList
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo
import org.techtown.find_gas_station.domain.repositoy.OilAvgRepository
import javax.inject.Inject

class OilAvgRepositoryImpl @Inject constructor (
    private val oilAvgRemoteDataSource : OilAvgRemoteDataSource
) : OilAvgRepository {

    override suspend fun getOilAvg(prodCd: String): Flow<List<OilAveragePriceInfo>> = flow {
        emit(oilAvgRemoteDataSource.getOilAvg(prodCd).toDomainList())
    }
}