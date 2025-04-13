package org.techtown.find_gas_station.data.datasource

import org.techtown.find_gas_station.BuildConfig
import org.techtown.find_gas_station.data.remote.api.KakaoApi
import org.techtown.find_gas_station.data.remote.api.OpinetApi
import org.techtown.find_gas_station.data.remote.model.station.detail.GasStationDetailInfoResult
import org.techtown.find_gas_station.data.remote.model.station.infomation.GasStationInfoResult
import org.techtown.find_gas_station.data.remote.model.station.kakao.Destination
import org.techtown.find_gas_station.data.remote.model.station.kakao.DirectionRequest
import org.techtown.find_gas_station.data.remote.model.station.kakao.DirectionResponse
import org.techtown.find_gas_station.data.remote.model.station.kakao.Origin
import javax.inject.Inject

class StationRemoteDataSource @Inject constructor(
    private val opinetApiService: OpinetApi,
    private val kakaoApiService: KakaoApi
) {

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