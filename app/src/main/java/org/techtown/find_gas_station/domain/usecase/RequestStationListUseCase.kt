package org.techtown.find_gas_station.domain.usecase

import android.util.Log
import kotlinx.coroutines.flow.Flow
import org.techtown.find_gas_station.domain.model.TotalOilInfo
import org.techtown.find_gas_station.di.RepositoryModule
import org.techtown.find_gas_station.domain.model.OilCondition

class RequestStationListUseCase {

    private val stationInfoRepository = RepositoryModule.provideStationInfoRepository()

    operator fun invoke(wgsX: String, wgsY: String, katecX: String, katecY: String ,oilCondition : OilCondition) : Flow<List<TotalOilInfo>> {
        Log.e("TAG","useCase")
        Log.e("TAG",oilCondition.radius)
        Log.e("TAG",oilCondition.sort)
        Log.e("TAG",oilCondition.oilKind)

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