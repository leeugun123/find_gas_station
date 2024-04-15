package org.techtown.find_gas_station.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.techtown.find_gas_station.Data.OilList.GasStationInfoResult
import org.techtown.find_gas_station.Data.TotalOilInfo
import org.techtown.find_gas_station.Data.kakao.Request.Destination
import org.techtown.find_gas_station.Data.kakao.Request.DirectionRequest
import org.techtown.find_gas_station.Data.kakao.Request.Origin
import org.techtown.find_gas_station.Data.kakao.Response.DirectionResponse
import org.techtown.find_gas_station.Data.kakao.Response.Route
import org.techtown.find_gas_station.Data.oilDetail.GasStationDetailInfoResult
import org.techtown.find_gas_station.R
import org.techtown.find_gas_station.util.api.ApiKey.OPI_API_KEY
import org.techtown.find_gas_station.util.api.Api_Instance.kakaoRetrofitApi
import org.techtown.find_gas_station.util.api.Api_Instance.opiRetrofitApi
import org.techtown.find_gas_station.util.comparator.OilRoadDistanceComparator
import org.techtown.find_gas_station.util.comparator.OilSpendTimeComparator
import org.techtown.find_gas_station.util.constant.ConstantGuide.JSON_FORMAT
import org.techtown.find_gas_station.util.constant.ConstantsTime.KAKAO_REQUEST_RADIUS
import org.techtown.find_gas_station.util.gps.GeoTrans
import org.techtown.find_gas_station.util.gps.GeoTransPoint
import java.util.Collections

class GetOilRepository() {

    private var tempList: MutableList<TotalOilInfo> = mutableListOf()

    private var wgsX = ""
    private var wgsY = ""

    fun getOilList() = tempList

    suspend fun requestOilList(
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

        val oilResponse = opiRetrofitApi.getOilList(
            OPI_API_KEY,
            JSON_FORMAT,
            katecX,
            katecY,
            radius,
            oilKind,
            sort
        )

        if (oilResponse.isSuccessful) {
            val oilResponse = oilResponse.body()
            val size = oilResponse?.oilInfoListResult?.oilInfoList?.size
            apiSizeCheck(oilResponse, size!!, oilKind, sort)
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
            handleOilListResponse(oilResponse, oilKind, sort)
    }// api 호출이 만료되면 빈 데이터가 들어옴. 따라서 만료되거나 점검하는지 체크하는 메소드


    private suspend fun handleOilListResponse(
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
            getOilDetail(
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

    private suspend fun getOilDetail(
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
            opiRetrofitApi.getOilDetail(OPI_API_KEY, JSON_FORMAT, uid)
        }

        if (response.isSuccessful)
            handleOilDetailResponse(
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

    private suspend fun handleOilDetailResponse(
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
            getOilKakaoApi(sort)

    }


    //카카오 api는 wgs 좌표를 사용
    private suspend fun getOilKakaoApi(sort: String) {

        val destinations = arrayOfNulls<Destination>(tempList.size)
        destinationsProcessing(destinations)

        val kakaoApiResponse = kakaoRetrofitApi.getMultiDirections(
            DirectionRequest(
                Origin(
                    wgsX.toDouble(), wgsY.toDouble()
                ),
                destinations, KAKAO_REQUEST_RADIUS
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
            R.string.ske.toString() -> R.drawable.sk
            R.string.gsx.toString() -> R.drawable.gs
            R.string.hdo.toString() -> R.drawable.hdoil
            R.string.sol.toString() -> R.drawable.so
            R.string.rto.toString(), R.string.rtx.toString() -> R.drawable.rto
            R.string.nho.toString() -> R.drawable.nho
            R.string.e1h.toString() -> R.drawable.e1
            R.string.skg.toString() -> R.drawable.skgas
            else -> R.drawable.oil_2
        }


    private fun getOilType(oilKind: String) =
        when (oilKind) {
            R.string.gasoline_code.toString() -> R.string.gasoline.toString()
            R.string.diesel_oil_code.toString() -> R.string.diesel_oil.toString()
            R.string.premium_gasoline_code.toString() -> R.string.premium_gasoline.toString()
            R.string.indoor_kerosene_code.toString() -> R.string.indoor_kerosene.toString()
            else -> R.string.car_butane.toString()
        }

    private fun listClear() {
        tempList.clear()
    }

    companion object {
        private const val KAKAO_API_PARAMETER_LIMIT = 30
    }


}