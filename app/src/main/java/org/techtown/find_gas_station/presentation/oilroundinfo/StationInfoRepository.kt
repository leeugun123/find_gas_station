package org.techtown.find_gas_station.presentation.oilroundinfo

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.data.OilList.GasStationInfoResult
import org.techtown.find_gas_station.data.TotalOilInfo
import org.techtown.find_gas_station.data.oilDetail.GasStationDetailInfoResult
import org.techtown.find_gas_station.kakao.Request.Destination
import org.techtown.find_gas_station.kakao.Request.DirectionRequest
import org.techtown.find_gas_station.kakao.Request.Origin
import org.techtown.find_gas_station.kakao.Response.DirectionResponse
import org.techtown.find_gas_station.kakao.Response.Route
import org.techtown.find_gas_station.util.api.ApiInstance
import org.techtown.find_gas_station.util.api.ApiKey
import org.techtown.find_gas_station.util.comparator.OilRoadDistanceComparator
import org.techtown.find_gas_station.util.comparator.OilSpendTimeComparator
import org.techtown.find_gas_station.util.constant.ConstantsTime
import org.techtown.find_gas_station.util.gps.GeoTrans
import org.techtown.find_gas_station.util.gps.GeoTransPoint
import java.util.Collections

class StationInfoRepository {

    private var tempList: MutableList<TotalOilInfo> = mutableListOf()

    private var wgsX = ""
    private var wgsY = ""

    fun getStationList() = tempList

    suspend fun requestStationList(
        wgsX: String,
        wgsY: String,
        katecX: String,
        katecY: String,
        radius: String,
        sort: String,
        oilKind: String
    ) {

        initWgsPos(wgsX, wgsY)
        listClear()

        val stationResponse = ApiInstance.opiRetrofitApi.getStationList(
            ApiKey.OPI_API_KEY,
            "json",
            katecX,
            katecY,
            radius,
            oilKind,
            sort
        )

        if (stationResponse.isSuccessful) {
            val size = stationResponse.body()?.oilInfoListResult?.oilInfoList?.size
            apiSizeCheck(stationResponse.body(), size!!, oilKind, sort)
        }
    }

    private fun initWgsPos(wgsX: String, wgsY: String) {
        this.wgsX = wgsX
        this.wgsY = wgsY
    }

    private suspend fun apiSizeCheck(
        oilResponse: GasStationInfoResult?,
        size: Int,
        oilKind: String,
        sort: String
    ) {
        if (size > 0)
            handleStationListResponse(oilResponse, oilKind, sort)
    }// api 호출이 만료되면 빈 데이터가 들어옴. 따라서 만료되거나 점검하는지 체크하는 메소드


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

        val response = withContext(Dispatchers.IO) {
            ApiInstance.opiRetrofitApi.getStationDetail(ApiKey.OPI_API_KEY, "json", uid)
        }

        if (response.isSuccessful)
            handleStationDetailResponse(
                response.body(),
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

    private suspend fun handleStationDetailResponse(
        gasStationDetailInfo: GasStationDetailInfoResult?, sort: String, size: Int,
        uid: String, name: String, gasPrice: String, distance: String, inputOil: String,
        imageResource: Int, destinationX: Float, destinationY: Float
    ) {
        gasStationDetailInfo?.let {

            val oilDetailInfo = it.gasStationDetailInfoResult.gasStationDetailInfo

            tempList.add(
                TotalOilInfo(
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
        if ((tempList.size == size || tempList.size == KAKAO_API_PARAMETER_LIMIT) &&
            (sort == "3" || sort == "4")
        )
            getStationKakaoApi(sort)
    }


    //카카오 api는 wgs 좌표를 사용
    private suspend fun getStationKakaoApi(sort: String) {

        val destinations = arrayOfNulls<Destination>(tempList.size)
        destinationsProcessing(destinations)

        val kakaoApiResponse = ApiInstance.kakaoRetrofitApi.getMultiDirections(
            DirectionRequest(
                Origin(
                    wgsX.toDouble(), wgsY.toDouble()
                ),
                destinations, ConstantsTime.KAKAO_REQUEST_RADIUS
            )
        )

        if (kakaoApiResponse.isSuccessful)
            handleKakaoApiResponse(kakaoApiResponse.body(), sort)
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
            tempList[i].actDistance = routes[i].summary.distance
            tempList[i].spendTime = routes[i].summary.duration
        }
    }


    private fun checkRoadOrSpend(sort: String) {
        if (sort == "4")
            Collections.sort(tempList, OilSpendTimeComparator())
        else
            Collections.sort(tempList, OilRoadDistanceComparator())
    }

    private fun getTrademarkImageResource(trademark: String) =
        when (trademark) {
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

    private fun getOilType(oilKind: String) =
        when (oilKind) {
            "B027" -> "휘발유"
            "D047" -> "경유"
            "B034" -> "고급 휘발유"
            "C004" -> "실내 등유"
            else -> "자동차 부탄"
        }

    private fun listClear() {
        tempList.clear()
    }

    companion object {
        private const val KAKAO_API_PARAMETER_LIMIT = 30
    }
}