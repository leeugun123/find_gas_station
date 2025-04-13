package org.techtown.find_gas_station.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.data.datasource.StationRemoteDataSource
import org.techtown.find_gas_station.data.remote.model.station.detail.GasStationDetailInfoResult
import org.techtown.find_gas_station.data.remote.model.station.infomation.GasStationInfoResult
import org.techtown.find_gas_station.data.remote.model.station.kakao.Destination
import org.techtown.find_gas_station.data.remote.model.station.kakao.DirectionResponse
import org.techtown.find_gas_station.data.remote.model.station.kakao.Origin
import org.techtown.find_gas_station.data.remote.model.station.kakao.Route
import org.techtown.find_gas_station.domain.model.StationDetailInfo
import org.techtown.find_gas_station.domain.repositoy.StationInfoRepository
import org.techtown.find_gas_station.presentation.common.util.comparator.OilRoadDistanceComparator
import org.techtown.find_gas_station.presentation.common.util.comparator.OilSpendTimeComparator
import org.techtown.find_gas_station.presentation.common.util.gps.GeoTrans
import org.techtown.find_gas_station.presentation.common.util.gps.GeoTransPoint
import java.util.Collections
import javax.inject.Inject

class StationInfoRepositoryImpl @Inject constructor(
    private val stationRemoteDataSource: StationRemoteDataSource
) : StationInfoRepository {

    private var tempList = mutableListOf<StationDetailInfo>()
    private var wgsX: String? = ""
    private var wgsY: String? = ""

    override fun requestStationList(
        wgsX: String,
        wgsY: String,
        katecX: String,
        katecY: String,
        radius: String,
        sort: String,
        oilKind: String
    ): Flow<List<StationDetailInfo>> = flow {
        initWgsPos(wgsX, wgsY)
        tempList.clear()

        val stationResponse =
            stationRemoteDataSource.fetchStationList(katecX, katecY, radius, sort, oilKind)

        stationResponse?.oilInfoListResult?.oilInfoList?.let { oilInfoList ->
            if (oilInfoList.isNotEmpty()) {
                handleStationListResponse(stationResponse, oilKind, sort)
                emit(tempList)
            }
        } ?: emit(emptyList())
    }

    private fun initWgsPos(wgsX: String, wgsY: String) {
        this.wgsX = wgsX
        this.wgsY = wgsY
    }

    private suspend fun handleStationListResponse(
        gasStationData: GasStationInfoResult?,
        oilKind: String,
        sort: String
    ) {
        val result = gasStationData?.let { adjustSize(it) }
        val inputOil = getOilType(oilKind)

        result?.forEach { oilInfo ->
            val out = GeoTrans.convert(
                GeoTrans.KATEC,
                GeoTrans.GEO,
                GeoTransPoint(oilInfo.gisX.toDouble(), oilInfo.gisY.toDouble())
            )
            getStationDetail(
                sort,
                result.size,
                oilInfo.id,
                oilInfo.osName,
                oilInfo.price,
                oilInfo.distance,
                inputOil,
                getTrademarkImageResource(oilInfo.pollDivCd),
                out.x.toFloat(),
                out.y.toFloat()
            )
        }
    }

    private fun adjustSize(it: GasStationInfoResult) =
        if (KAKAO_API_PARAMETER_LIMIT < it.oilInfoListResult.oilInfoList.size)
            it.oilInfoListResult.oilInfoList.take(KAKAO_API_PARAMETER_LIMIT)
        else
            it.oilInfoListResult.oilInfoList

    private suspend fun getStationDetail(
        sort: String,
        size: Int,
        uid: String,
        name: String,
        gasPrice: String,
        distance: String,
        inputOil: String,
        imageResource: Int,
        destinationX: Float,
        destinationY: Float
    ) {
        val response = stationRemoteDataSource.fetchStationDetail(uid)

        response?.let {
            handleStationDetailResponse(
                response,
                sort,
                size,
                uid,
                name,
                gasPrice,
                distance,
                inputOil,
                imageResource,
                destinationX,
                destinationY
            )
        }
    }

    private suspend fun handleStationDetailResponse(
        gasStationDetailInfo: GasStationDetailInfoResult?, sort: String, size: Int,
        uid: String, name: String, gasPrice: String, distance: String, inputOil: String,
        imageResource: Int, destinationX: Float, destinationY: Float
    ) {
        gasStationDetailInfo?.let {
            val oilDetailInfo = it.gasStationDetailInfoResult.gasStationDetailInfo

            if (oilDetailInfo.isEmpty())
                return

            tempList.add(
                StationDetailInfo(
                    uid,
                    name,
                    gasPrice,
                    distance.toDouble().toInt().toString(),
                    inputOil,
                    imageResource,
                    destinationX,
                    destinationY,
                    oilDetailInfo[0].carWashExist,
                    oilDetailInfo[0].conStoreExist,
                    oilDetailInfo[0].address,
                    oilDetailInfo[0].streetAddress,
                    oilDetailInfo[0].calNumber,
                    oilDetailInfo[0].sector,
                    "",
                    ""
                )
            )

            checkTempListSize(size, sort)
        }
    }

    private suspend fun checkTempListSize(size: Int, sort: String) {
        if (isKakaoApiRequired(size, sort))
            getStationKakaoApi(sort)
    }

    private fun isKakaoApiRequired(size: Int, sort: String) =
        ((tempList.size == size || tempList.size == KAKAO_API_PARAMETER_LIMIT) && (sort == "3" || sort == "4"))

    private suspend fun getStationKakaoApi(sort: String) {
        val destinations = arrayOfNulls<Destination>(tempList.size)
        destinationsProcessing(destinations)

        val kakaoApiResponse = stationRemoteDataSource.fetchKakaoDirections(
            Origin(wgsX!!.toDouble(), wgsY!!.toDouble()), destinations
        )

        kakaoApiResponse?.let {
            handleKakaoApiResponse(it, sort)
        }
    }

    private fun destinationsProcessing(destinations: Array<Destination?>) {
        for (i in tempList.indices) {
            val uid = tempList[i].uid
            val wgsX = tempList[i].wgs84X.toDouble()
            val wgsY = tempList[i].wgs84Y.toDouble()
            destinations[i] = Destination(uid, wgsX, wgsY)
        }
    }

    private fun handleKakaoApiResponse(directionResponse: DirectionResponse?, sort: String) {
        directionResponse?.let {
            insertPlusList(it.routes)
            checkRoadOrSpend(sort)
        }
    }

    private fun insertPlusList(routes: List<Route>) {
        for (i in tempList.indices) {
            tempList[i].actualDistance = routes[i].summary.distance
            tempList[i].spendTime = routes[i].summary.duration
        }
    }

    private fun checkRoadOrSpend(sort: String) {
        if (sort == "4") {
            Collections.sort(tempList, OilSpendTimeComparator())
        } else {
            Collections.sort(tempList, OilRoadDistanceComparator())
        }
    }

    private fun getTrademarkImageResource(trademark: String) = when (trademark) {
        "SKE" -> R.drawable.sk
        "GSC" -> R.drawable.gs
        "HDO" -> R.drawable.hdoil
        "SOL" -> R.drawable.so
        "RTO", "RTX" -> R.drawable.rto
        "NHO" -> R.drawable.nho
        "E1G" -> R.drawable.e1
        "SKG" -> R.drawable.skgas
        else -> R.drawable.oil_2
    }

    private fun getOilType(oilKind: String) = when (oilKind) {
        "B027" -> "휘발유"
        "D047" -> "경유"
        "B034" -> "고급 휘발유"
        "C004" -> "실내 등유"
        else -> "자동차 부탄"
    }

    companion object {
        private const val KAKAO_API_PARAMETER_LIMIT = 30
    }
}