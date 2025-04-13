package org.techtown.find_gas_station.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import org.techtown.find_gas_station.domain.model.OilCondition
import org.techtown.find_gas_station.domain.model.StationDetailInfo
import org.techtown.find_gas_station.domain.repositoy.StationInfoRepository
import javax.inject.Inject

class RequestStationListUseCase @Inject constructor(
    private val stationInfoRepository: StationInfoRepository
) {
    operator fun invoke(
        wgsX: String,
        wgsY: String,
        katecX: String,
        katecY: String,
        oilCondition: OilCondition
    ): Flow<List<StationDetailInfo>> {
        return stationInfoRepository.requestStationList(
            wgsX,
            wgsY,
            katecX,
            katecY,
            oilCondition.radius,
            oilCondition.sort,
            oilCondition.oilKind
        )
    }
}