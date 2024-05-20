package org.techtown.find_gas_station.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.techtown.find_gas_station.data.datasource.OilAvgRemoteDataSource
import org.techtown.find_gas_station.data.remote.model.oilavg.OilAveragePriceInfo

class OilAvgRepository() {

    private val oilAvgRemoteDataSource = OilAvgRemoteDataSource()
    suspend fun getOilAvg(prodCd: String): Flow<List<OilAveragePriceInfo>> = flow {
        emit(oilAvgRemoteDataSource.getOilAvg(prodCd))
    }
}