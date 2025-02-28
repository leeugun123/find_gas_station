package org.techtown.find_gas_station.domain.repositoy

import kotlinx.coroutines.flow.Flow
import org.techtown.find_gas_station.domain.model.TotalOilInfo

interface StationInfoRepository {
    fun requestStationList(
        wgsX: String,
        wgsY: String,
        katecX: String,
        katecY: String,
        radius: String,
        sort: String,
        oilKind: String
    ): Flow<List<TotalOilInfo>>
}