package org.techtown.find_gas_station.domain.repositoy

import kotlinx.coroutines.flow.Flow
import org.techtown.find_gas_station.domain.model.OilAveragePriceInfo

interface OilAvgRepository {
    suspend fun getOilAvg(prodCd: String): Flow<List<OilAveragePriceInfo>>
}