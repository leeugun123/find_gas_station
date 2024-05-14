package org.techtown.find_gas_station.data.datasource

import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.data.remote.model.station.GasStationInfoResult
import org.techtown.find_gas_station.data.remote.model.station.kakao.request.Destination
import org.techtown.find_gas_station.data.remote.model.station.kakao.request.DirectionRequest
import org.techtown.find_gas_station.data.remote.model.station.kakao.request.Origin
import org.techtown.find_gas_station.data.remote.model.station.kakao.response.DirectionResponse
import org.techtown.find_gas_station.data.remote.model.station.oilDetail.GasStationDetailInfoResult
import org.techtown.find_gas_station.presentation.di.ApiModule

class StationRemoteDataSource {

    private val opinetApiService = ApiModule.provideOpinetApi()
    private val kakaoApiService = ApiModule.provideKakaoApi()

    suspend fun fetchStationList(
        katecX: String,
        katecY: String,
        radius: String,
        sort: String,
        oilKind: String
    ): GasStationInfoResult? {

        val response = opinetApiService.getStationList(
            BuildConfig.GAS_API_KEY,
            "json",
            katecX,
            katecY,
            radius,
            oilKind,
            sort
        )
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun fetchStationDetail(uid: String): GasStationDetailInfoResult? {
        val response = opinetApiService.getStationDetail(BuildConfig.GAS_API_KEY, "json", uid)
        return if (response.isSuccessful) response.body() else null
    }

    suspend fun fetchKakaoDirections(
        origin: Origin,
        destinations: Array<Destination?>
    ): DirectionResponse? {
        val response = kakaoApiService.getMultiDirections(
            DirectionRequest(
                origin,
                destinations,
                KAKAO_REQUEST_RADIUS
            )
        )
        return if (response.isSuccessful) response.body() else null
    }

    companion object {
        private const val KAKAO_REQUEST_RADIUS = 10000
    }

}